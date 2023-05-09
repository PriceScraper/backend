package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.dtos.ShopItem;
import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import be.xplore.pricescraper.exceptions.ScraperNotFoundException;
import be.xplore.pricescraper.exceptions.TrackItemException;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.ShopRepository;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
            log.info(
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
      log.warn("Failed to find scraper for tracked item " + trackedItem.getUrl());
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

  public List<ItemSearchDto> findItemByNameLike(String name) {
    return itemRepository.findItemsByNameLike(name);
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
    var scraperDomain = scraperService.getScraperRootDomain(urlToItem);
    if (scraperDomain.isEmpty()) {
      throw new TrackItemException("Scraper not found while tracking item");
    }
    var scrapedResponse = scraperService.scrapeFullUrl(urlToItem);
    if (scrapedResponse.isEmpty()) {
      throw new TrackItemException("Failed to scrape item");
    }
    var shop = getShopFromDomain(scraperDomain.get());
    var itemIdentifier = scraperService.getItemIdentifier(urlToItem);
    if (itemIdentifier.isPresent()
        &&
        trackedItemRepository.existsByUrlIgnoreCaseAndShopId(itemIdentifier.get(),
            shop.getId())) {
      throw new TrackItemException("Item is already being tracked");
    }
    var item = getItem(scrapedResponse.get().title(), "", 1, Optional.empty());
    var trackedItem = getTrackedItem(urlToItem, item, shop);
    if (trackedItem.isEmpty()) {
      throw new TrackItemException("Failed to create tracked item");
    }
    storeItemPrice(trackedItem.get(), scrapedResponse.get().price());
    return trackedItem.get();
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
  private Optional<TrackedItem> getTrackedItem(String url, Item item, Shop shop) {
    var identifier = scraperService.getItemIdentifier(url);
    if (identifier.isEmpty()) {
      return Optional.empty();
    }
    var trackedItem = new TrackedItem();
    trackedItem.setUrl(identifier.get());
    trackedItem.setShop(shop);
    trackedItem.setItem(item);
    trackedItem.setLastAttempt(Timestamp.from(Instant.MIN));
    return Optional.of(trackedItemRepository.save(trackedItem));
  }

  public long trackedItemsCount() {
    return trackedItemRepository.count();
  }
}
