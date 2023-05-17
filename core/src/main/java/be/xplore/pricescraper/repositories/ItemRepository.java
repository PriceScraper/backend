package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Item}.
 */
@Repository
public interface ItemRepository {
  List<ItemSearchDto> findItemsByNameLike(String name);

  List<Item> findItemByNameWithFuzzySearchAndLimit(String nameQuery, int limit);

  Optional<Item> findItemWithTrackedItemsById(int id);

  List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems);

  List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems,
                                                  LocalDateTime since);

  Item save(Item item);

  Optional<Item> findById(int id);
}
