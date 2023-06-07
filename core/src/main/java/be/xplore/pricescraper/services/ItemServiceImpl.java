package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.domain.shops.UnitType;
import be.xplore.pricescraper.dtos.ItemAmountDetails;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.dtos.ShopItem;
import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import be.xplore.pricescraper.exceptions.ScrapeItemException;
import be.xplore.pricescraper.exceptions.ScraperNotFoundException;
import be.xplore.pricescraper.exceptions.TrackItemException;
import be.xplore.pricescraper.matchers.Combiner;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.ShopRepository;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This service will handle service operations for {@link Item}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;
  private final ItemPriceRepository itemPriceRepository;
  private final TrackedItemRepository trackedItemRepository;
  private final ShopRepository shopRepository;
  private final ScraperService scraperService;
  private final Combiner combiner;

  /**
   * Find by key.
   */
  public Item findItemById(int id) {
    return itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
  }


  /**
   * Find {@link Item}s with {@link TrackedItem}s and their latest {@link ItemPrice}s respectively.
   *
   * @param id Id of item
   * @return {@link Item}
   */
  public Item findItemWithTrackedItemsAndLatestPricesById(int id) {
    Item item =
        itemRepository.findItemWithTrackedItemsById(id).orElseThrow(ItemNotFoundException::new);
    List<ItemPrice> itemPrices =
        itemRepository.findLatestPricesForTrackedItems(item.getTrackedItems(),
            LocalDateTime.now().minusDays(7));
    assignPricesToTrackedItemsOfItems(item, itemPrices);
    return item;
  }

  /**
   * Find {@link Item}s with {@link TrackedItem}s and their latest {@link ItemPrice} respectively.
   *
   * @param id Id of item
   * @return {@link Item}
   */
  public Item findItemWithTrackedItemsAndLatestPriceById(int id) {
    Item item =
        itemRepository.findItemWithTrackedItemsById(id).orElseThrow(ItemNotFoundException::new);
    List<ItemPrice> itemPrices =
        itemRepository.findLatestPricesForTrackedItems(item.getTrackedItems());
    assignLatestPriceToTrackedItemsOfItems(item, itemPrices);
    return item;
  }


  /**
   * Find by name.
   * If low amount of results, start discovering new items.
   */
  public List<ItemSearchDto> findItemByNameLike(String name) {
    var res = itemRepository.findItemByNameLike(name);
    if (res.size() < 5 && name.strip().length() > 2) {
      var newItems = discoverNewItems(name);
      if (newItems.size() > 0) {
        return itemRepository.findItemByNameLike(name);
      }
    }
    return res;
  }

  /**
   * mapping of item prices in memory.
   */
  private void assignPricesToTrackedItemsOfItems(Item item, List<ItemPrice> itemPrices) {
    for (TrackedItem trackedItem : item.getTrackedItems()) {
      trackedItem.setItemPrices(
          itemPrices.stream().filter(p -> p.getTrackedItem().getUrl().equals(trackedItem.getUrl()))
              .toList());
    }
  }

  /**
   * mapping of item prices in memory.
   */
  private void assignLatestPriceToTrackedItemsOfItems(Item item, List<ItemPrice> itemPrices) {
    for (ItemPrice itemPrice : itemPrices) {
      for (TrackedItem trackedItem : item.getTrackedItems()) {
        if (itemPrice.getTrackedItem().getUrl().equals(trackedItem.getUrl())) {
          trackedItem.setItemPrices(List.of(itemPrice));
          break;
        }
      }
    }
  }

  /**
   * Items which should be scraped again.
   *
   * @return the items which have the oldest price date.
   */
  private List<TrackedItem> oldestTrackedItems(int limit) {
    return trackedItemRepository.findAllSortedByOldestFirst(limit).stream().toList();
  }

  /**
   * Scrapes the items with the oldest data.
   */
  @Override
  public void updateOldestTrackedItems(int limit) {
    var itemsToTrack = oldestTrackedItems(limit);
    itemsToTrack.forEach(item -> {
      if (hasBeenScrapedRecently(item)) {
        log.debug(
            "Last attempt: " + item.getLastAttempt() + ", skipping scrape of " + item.getUrl());
      } else {
        scrapeTrackedItem(item);
      }
    });
  }

  /**
   * Execute scrape of item.
   *
   * @param trackedItem item to scrape.
   */
  private void scrapeTrackedItem(TrackedItem trackedItem) {
    try {
      var start = Timestamp.from(Instant.now());
      var scraped = scraperService.scrapeTrackedItem(trackedItem);
      modifyTrackedItemPrice(trackedItem, scraped);
      var end = Timestamp.from(Instant.now());
      log.info("Last attempt: "
          + trackedItem.getLastAttempt()
          + ", took "
          + (end.getTime() - start.getTime())
          + "ms to scrape "
          + trackedItem.getUrl());
    } catch (ScraperNotFoundException scraperNotFoundException) {
      setLastAttemptToNow(trackedItem);
      log.error("Failed to find scraper for tracked item " + trackedItem.getUrl());
    }
  }

  /**
   * Checks if the item has been scraped recently.
   *
   * @param trackedItem item to check.
   * @return true if it has been scraped recently.
   */
  private boolean hasBeenScrapedRecently(TrackedItem trackedItem) {
    return trackedItem.getLastAttempt() != null && !trackedItem.getLastAttempt()
        .before(Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS)));
  }


  /**
   * Modify tracked item price.
   * Set last attempt time to now.
   */
  @Transactional
  public void modifyTrackedItemPrice(TrackedItem trackedItem, Optional<ShopItem> shopItem) {
    if (shopItem.isPresent()) {
      storeItemPrice(trackedItem, shopItem.get().price());
    } else {
      log.warn("No price found for " + trackedItem);
      setLastAttemptToNow(trackedItem);
    }
  }

  /**
   * Set last attempt to now.
   */
  private void setLastAttemptToNow(TrackedItem trackedItem) {
    trackedItem.setLastAttempt(Timestamp.from(Instant.now()));
    trackedItemRepository.save(trackedItem);
  }

  /**
   * Store item price.
   * Set last attempt of item to now.
   */
  private void storeItemPrice(TrackedItem item, double price) {
    var p = new ItemPrice();
    p.setPrice(price);
    p.setTrackedItem(item);
    p.setTimestamp(Instant.now());
    itemPriceRepository.save(p);
    setLastAttemptToNow(item);
  }

  /**
   * Set up item tracking by URL.
   */
  @Transactional
  public TrackedItem addTrackedItem(String urlToItem) {
    var scraperDomain = scraperService.getScraperRootDomain(urlToItem);
    var scrapedResponse =
        scraperService.scrapeFullUrl(urlToItem).orElseThrow(ScrapeItemException::new);
    var shop = getShopFromDomain(scraperDomain);
    var dbItem = trackedItemRepository.findByUrlIgnoreCaseAndShopId(urlToItem, shop.getId());
    if (dbItem.isPresent()) {
      log.debug("Item is already being tracked. Url: " + dbItem.get().getUrl());
      return dbItem.get();
    }
    var amountDetails = scrapedResponse.details()
        .orElse(new ItemAmountDetails(UnitType.NOT_AVAILABLE, 1, 1));
    var item =
        getItem(scrapedResponse.title(), scrapedResponse.img().orElse(null), amountDetails,
            scrapedResponse.ingredients().orElse(null),
            scrapedResponse.nutritionValues().orElse(null));
    var trackedItem = getTrackedItem(urlToItem, item, shop, scrapedResponse.price()).orElseThrow(
        TrackItemException::new);
    addTrackedItemToItem(item, trackedItem);
    return trackedItem;
  }


  /**
   * Retrieve shop or create if does not exist.
   */
  private Shop getShopFromDomain(String rootDomain) {
    var shop = shopRepository.findByUrl(rootDomain);
    if (shop.isPresent()) {
      return shop.get();
    }
    var tempShop = new Shop();
    tempShop.setUrl(rootDomain);
    tempShop.setName(rootDomain);
    return shopRepository.save(tempShop);
  }

  /**
   * Retrieve shop or create if does not exist.
   */
  private Item getItem(String title, String img, ItemAmountDetails amountDetails,
                       String ingredients, Map<String, String> nutritionValues) {
    Item item = getExistingItemIfMatches(title, img, amountDetails, ingredients, nutritionValues);
    if (item == null) {
      item = addNewItem(title, img, amountDetails, ingredients, nutritionValues);
    }
    return item;
  }

  private Item getExistingItemIfMatches(String title, String img, ItemAmountDetails amountDetails,
                                        String ingredients, Map<String, String> nutritionValues) {
    Item itemToMatch =
        new Item(title, img, amountDetails.quantity(), amountDetails.type(), amountDetails.amount(),
            ingredients, nutritionValues);
    List<Item> potentialMatches = getPotentialMatchingItems();
    for (Item potentialMatch : potentialMatches) {
      combiner.addItems(potentialMatch, itemToMatch);
      if (combiner.isMatching()) {
        return potentialMatch;
      }
    }
    return null;
  }

  private List<Item> getPotentialMatchingItems() {
    return itemRepository.findAll();
  }

  private Item addNewItem(String title, String img, ItemAmountDetails amountDetails,
                          String ingredients, Map<String, String> nutritionValues) {
    Item item = new Item();
    item.setName(title);
    item.setImage(img);
    item.setType(amountDetails.type());
    item.setAmount(amountDetails.amount());
    item.setQuantity(amountDetails.quantity());
    item.setIngredients(ingredients);
    item.setNutritionValues(nutritionValues);
    item = itemRepository.save(item);
    return item;
  }

  /**
   * Create tracked item.
   */
  private Optional<TrackedItem> getTrackedItem(String url, Item item, Shop shop, double price) {
    var trackedItem = new TrackedItem();
    trackedItem.setUrl(url);
    trackedItem.setShop(shop);
    trackedItem.setItem(item);
    trackedItem.setLastAttempt(Timestamp.from(Instant.now()));
    trackedItem = trackedItemRepository.save(trackedItem);
    trackedItem.setItemPrices(new ArrayList<>());
    trackedItem.getItemPrices().add(getItemPriceForTrackedItem(price, trackedItem));
    trackedItem = trackedItemRepository.save(trackedItem);
    return Optional.of(trackedItem);
  }

  private ItemPrice getItemPriceForTrackedItem(double price, TrackedItem trackedItem) {
    ItemPrice itemPrice = new ItemPrice();
    itemPrice.setPrice(price);
    itemPrice.setTimestamp(Instant.now());
    itemPrice.setTrackedItem(trackedItem);
    return itemPriceRepository.save(itemPrice);
  }

  private void addTrackedItemToItem(Item item, TrackedItem trackedItem) {
    List<TrackedItem> trackedItemsToAdd = new ArrayList<>();
    if (item.getTrackedItems() != null) {
      trackedItemsToAdd.addAll(item.getTrackedItems());
    }
    trackedItemsToAdd.add(trackedItem);
    item.setTrackedItems(trackedItemsToAdd);
    itemRepository.save(item);
  }


  /**
   * Step 1. Get potential items.
   * Step 2. Scrape each item separately and add them to db.
   */
  public List<TrackedItem> discoverNewItems(String query) {
    var start = LocalDateTime.now();
    var potentialItems = getDiscoveredItems(query);
    var trackedItems = new ArrayList<TrackedItem>();
    var itemsSkipped = 0;
    log.info(String.format("Found %d potential items for query: %s", potentialItems.size(), query));
    for (var item : potentialItems) {
      try {
        if (trackedItemRepository.existsByUrlIgnoreCase(item.url())) {
          log.debug("Item already in db, skipped: " + item.url());
          itemsSkipped++;
        } else {
          var res = addTrackedItem(item.url());
          trackedItems.add(res);
        }
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
    logDiscoveryPerformance(start, trackedItems.size(), potentialItems.size() - itemsSkipped,
        query);
    return trackedItems;
  }


  /**
   * Step 1. Get potential items.
   */
  public List<ItemScraperSearch> getDiscoveredItems(String query) {
    return scraperService.discoverItems(query);
  }

  /**
   * Logs the duration to discover the new items.
   */
  private void logDiscoveryPerformance(LocalDateTime start, int itemsFound, int itemsTried,
                                       String query) {
    var duration = Duration.between(start, LocalDateTime.now()).getSeconds();
    var sentence =
        String.format("Took %ds to discover %d/%d items for query: %s", duration, itemsFound,
            itemsTried, query);
    if (duration > itemsTried * 0.5) {
      log.warn(sentence);
    } else {
      log.info(sentence);
    }
  }

  /**
   * Returns amount of items in table.
   */
  public long trackedItemsCount() {
    return trackedItemRepository.count();
  }
}
