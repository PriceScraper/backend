package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.dtos.ShopItem;
import java.util.List;
import java.util.Optional;

/**
 * This service will handle service operations for {@link Item}.
 */
public interface ItemService {

  Item findItemById(int id);

  void updateOldestTrackedItems(int limit);

  Item findItemWithTrackedItemsAndLatestPricesById(int id);

  List<ItemSearchDto> findItemByNameLike(String name);

  void modifyTrackedItemPrice(TrackedItem trackedItem, Optional<ShopItem> shopItem);

  void setLastAttemptToNow(TrackedItem trackedItem);

  TrackedItem addTrackedItem(String urlToItem);

  long trackedItemsCount();

  List<TrackedItem> discoverNewItems(String query);
}
