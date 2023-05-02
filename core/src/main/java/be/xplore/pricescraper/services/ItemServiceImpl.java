package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.dtos.ServiceResponse;
import be.xplore.pricescraper.dtos.ShopItem;
import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.ShopRepository;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
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
  public List<TrackedItem> oldestTrackedItems(int limit) {
    var sort = Sort.by("lastAttempt").ascending();
    var pageable = PageRequest
        .of(0, limit)
        .withSort(sort);
    return trackedItemRepository.findAll(pageable).stream().toList();
  }

  /**
   * Find {@link Item}s with {@link TrackedItem}s and their latest {@link ItemPrice} respectively.
   *
   * @param id Id of item
   * @return {@link Item}
   */
  public Item findItemWithTrackedItemsAndLatestPricesById(int id) {
    Item item = itemRepository.findItemWithTrackedItemsById(id);
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
    p.setTimestamp(Timestamp.from(Instant.now()));
    itemPriceRepository.save(p);
    setLastAttemptToNow(item);
  }

  /**
   * Set up item tracking by URL.
   */
  @Transactional
  public ServiceResponse<TrackedItem> addTrackedItem(String urlToItem) {
    var scraperDomain = scraperService.getScraperRootDomain(urlToItem);
    if (scraperDomain.isEmpty()) {
      return new ServiceResponse<>(false, null, "Scraper not found.");
    }
    var scrapedResponse = scraperService.scrapeFullUrl(urlToItem);
    if (scrapedResponse.isEmpty()) {
      return new ServiceResponse<>(false, null, "Failed to scrape item.");
    }
    var shop = getShopFromDomain(scraperDomain.get());
    var itemIdentifier = scraperService.getItemIdentifier(urlToItem);
    if (itemIdentifier.isPresent()
        &&
        trackedItemRepository.existsByUrlIgnoreCaseAndShopId(itemIdentifier.get(),
            shop.getId())) {
      return new ServiceResponse<>(false, null, "Item already being tracked.");
    }
    var item = getItem(scrapedResponse.get().title(), "", 1, Optional.empty());
    var trackedItem = getTrackedItem(urlToItem, item, shop);
    if (trackedItem.isEmpty()) {
      return new ServiceResponse<>(false, null, "Failed to create tracked item.");
    }
    storeItemPrice(trackedItem.get(), scrapedResponse.get().price());
    return new ServiceResponse<>(true, trackedItem.get(), null);
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
