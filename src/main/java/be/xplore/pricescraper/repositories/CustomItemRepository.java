package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Item;
import java.util.List;

/**
 * Custom repository interface for {@link Item}.
 * The implementation is {@link CustomItemRepositoryImpl}
 */
public interface CustomItemRepository {

  List<Item> findItemsWithTrackedItemsAndLastPriceByNameLike(String name);

}
