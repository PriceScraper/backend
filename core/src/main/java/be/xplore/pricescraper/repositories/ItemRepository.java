package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Item}.
 */
@Repository
public interface ItemRepository {
  List<ItemSearchDto> findItemsByNameLike(String name);

  Optional<Item> findItemWithTrackedItemsById(int id);

  List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems);

  Item save(Item item);

  Item saveAndFlush(Item item);

  Optional<Item> findById(int id);
}
