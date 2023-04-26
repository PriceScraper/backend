package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import java.util.List;

/**
 * Custom repository interface for {@link Item}.
 * The implementation is {@link CustomItemRepositoryImpl}
 */
public interface CustomItemRepository {

  List<ItemSearchDto> findItemsByNameLike(String name);

  Item findItemWithTrackedItemsById(int id);

  List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems);

}
