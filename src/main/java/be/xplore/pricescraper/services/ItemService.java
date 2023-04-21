package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
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

  public List<Item> findItemsWithTrackedItemsByNameLike(String name) {
    return itemRepository.findItemsWithTrackedItemsAndLastPriceByNameLike(name);
  }

}
