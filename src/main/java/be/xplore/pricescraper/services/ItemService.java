package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.repositories.ItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * This service will handle service operations for {@link Item}.
 */
@Service
public class ItemService {

  private final ItemRepository itemRepository;

  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
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

  //mapping of itemprices in memory
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

  public List<ItemSearchDto> findItemByNameLike(String name) {
    return itemRepository.findItemsByNameLike(name);
  }

}
