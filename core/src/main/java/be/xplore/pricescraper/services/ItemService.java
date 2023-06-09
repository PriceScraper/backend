package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import java.util.List;

/**
 * This service will handle service operations for {@link Item}.
 */
public interface ItemService {

  Item findItemById(int id);

  void updateOldestTrackedItems(int limit);

  Item findItemWithTrackedItemsAndLatestPriceById(int id);

  Item findItemWithTrackedItemsAndLatestPricesById(int id);

  List<ItemSearchDto> findItemByNameLike(String name);

  List<ItemScraperSearch> getDiscoveredItems(String query);

  TrackedItem addTrackedItem(String urlToItem);

  long trackedItemsCount();

  List<TrackedItem> discoverNewItems(String query);
}
