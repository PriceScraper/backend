package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.dtos.ShopItem;
import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import be.xplore.pricescraper.exceptions.RootDomainNotFoundException;
import be.xplore.pricescraper.exceptions.ScrapeItemException;
import be.xplore.pricescraper.exceptions.ScraperNotFoundException;
import be.xplore.pricescraper.exceptions.TrackItemException;
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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

  public Item findItemById(int id) {
    return itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
  }


  /**
   * Find {@link Item}s with {@link TrackedItem}s and their latest {@link ItemPrice} respectively.
   *
   * @param id Id of item
   * @return {@link Item}
   */
  public Item findItemWithTrackedItemsAndLatestPricesById(int id) {
    Item item =
        itemRepository.findItemWithTrackedItemsById(id).orElseThrow(ItemNotFoundException::new);
    List<ItemPrice> itemPrices =
        itemRepository.findLatestPricesForTrackedItems(item.getTrackedItems());
    assignPricesToTrackedItemsOfItems(item, itemPrices);
    return item;
  }


  /**
   * Find by name.
   * If low amount of results, start discovering new items.
   */
  public List<ItemSearchDto> findItemByNameLike(String name) {
    var res = itemRepository.findItemsByNameLike(name.strip());
    if (res.size() > 3 || name.strip().length() < 3) {
      return res;
    }
    discoverNewItems(name);
    return itemRepository.findItemsByNameLike(name.strip());
  }

  /**
   * mapping of item prices in memory.
   */
  private void assignPricesToTrackedItemsOfItems(Item item, List<ItemPrice> itemPrices) {
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
    var sort = Sort.by("lastAttempt").ascending();
    var pageable = PageRequest
        .of(0, limit)
        .withSort(sort);
    return trackedItemRepository.findAll(pageable).stream().toList();
  }

  /**
   * Scrapes the items with the oldest data.
   */
  @Override
  public void updateOldestTrackedItems(int limit) {
    var itemsToTrack = oldestTrackedItems(limit);
    itemsToTrack
        .forEach(item -> {
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
    return trackedItem.getLastAttempt() != null
        && !trackedItem.getLastAttempt().before(
        Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS)));
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
  public void setLastAttemptToNow(TrackedItem trackedItem) {
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
    var scraperDomain = scraperService.getScraperRootDomain(urlToItem).orElseThrow(
        RootDomainNotFoundException::new);
    var scrapedResponse =
        scraperService.scrapeFullUrl(urlToItem).orElseThrow(ScrapeItemException::new);
    var shop = getShopFromDomain(scraperDomain);
    var itemIdentifier =
        scraperService.getItemIdentifier(urlToItem).orElseThrow(ScrapeItemException::new);
    var dbItem =
        trackedItemRepository.findByUrlIgnoreCaseAndShopId(itemIdentifier, shop.getId());
    if (dbItem.isPresent()) {
      log.debug("Item is already being tracked. Url: " + dbItem.get().getUrl());
      return dbItem.get();
    }
    var item =
        getItem(scrapedResponse.title(), scrapedResponse.img().orElse(null), 1,
            scrapedResponse.ingredients());
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
  private Item getItem(String title, String img, int quantity, Optional<String> ingredients) {
    var item = new Item();
    item.setName(title);
    item.setImage(img);
    item.setQuantity(quantity);
    item.setIngredients(ingredients.orElse(null));
    return itemRepository.save(item);
  }

  /**
   * Create tracked item.
   */
  private Optional<TrackedItem> getTrackedItem(String url, Item item, Shop shop, double price) {
    var identifier = scraperService.getItemIdentifier(url);
    if (identifier.isEmpty()) {
      return Optional.empty();
    }
    var trackedItem = new TrackedItem();
    trackedItem.setUrl(identifier.get());
    trackedItem.setShop(shop);
    trackedItem.setItem(item);
    trackedItem.setLastAttempt(Timestamp.from(Instant.MIN));
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

  private void addTrackedItemToItem(Item item,
                                    TrackedItem trackedItem) {
    List<TrackedItem> trackedItemToAdd = new ArrayList<>();
    trackedItemToAdd.add(trackedItem);
    if (item.getTrackedItems() != null) {
      item.getTrackedItems().addAll(trackedItemToAdd);
    } else {
      item.setTrackedItems(trackedItemToAdd);
    }
    itemRepository.save(item);
  }


  /**
   * Step 1. Get potential items.
   * Step 2. Scrape each item separately and add them to db.
   */
  public List<TrackedItem> discoverNewItems(String query) {
    var start = LocalDateTime.now();
    var potentialItems = scraperService.discoverItems(query);
    var trackedItems = new ArrayList<TrackedItem>();
    for (var item : potentialItems) {
      try {
        var res = addTrackedItem(item.url());
        trackedItems.add(res);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
    logDiscoveryPerformance(start, trackedItems.size(), potentialItems.size(), query);
    return trackedItems;
  }

  /**
   * Logs the duration to discover the new items.
   */
  private void logDiscoveryPerformance(LocalDateTime start, int itemsFound, int itemsTried,
                                       String query) {
    var duration = Duration.between(start, LocalDateTime.now()).getSeconds();
    var sentence =
        String.format("Took %ds to discover %d/%d items for query: %s",
            duration, itemsFound, itemsTried, query);
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
