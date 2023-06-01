package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Item}.
 */
public interface ItemRepository {

  List<Item> findAll();

  List<ItemSearchDto> findItemByNameWithFuzzySearchAndLimit(String nameQuery, int limit);

  Optional<Item> findItemWithTrackedItemsById(int id);

  List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems);

  List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems,
                                                  LocalDateTime since);

  Item save(Item item);

  Optional<Item> findById(int id);
}
