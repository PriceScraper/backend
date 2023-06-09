package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.shops.ItemEntity;
import be.xplore.pricescraper.entity.shops.ItemPriceEntity;
import be.xplore.pricescraper.entity.shops.TrackedItemEntity;
import be.xplore.pricescraper.repositories.ItemRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * JPA repository implementation of {@link ItemRepository}.
 */
@Repository
public interface ItemJpaRepository
    extends JpaRepository<ItemEntity, Integer> {

  @Query("SELECT i FROM Item i LEFT JOIN FETCH i.trackedItems")
  List<ItemEntity> findAllWithTrackedItems();

  List<ItemEntity> findByNameContainsIgnoreCase(String name);


  @Query("""
              SELECT i from Item i
              LEFT JOIN FETCH i.trackedItems
              WHERE i.id = :id
      """)
  Optional<ItemEntity> findItemWithTrackedItemsById(int id);

  @Query("""
              SELECT DISTINCT ip from ItemPrice ip
              WHERE ip.trackedItem in :trackedItems
              AND ip.timestamp = (SELECT MAX(ip2.timestamp)
               FROM ItemPrice ip2
              WHERE ip2.trackedItem = ip.trackedItem)
      """)
  List<ItemPriceEntity> findLatestPricesForTrackedItems(List<TrackedItemEntity> trackedItems);

  @Query("""
              SELECT DISTINCT ip from ItemPrice ip
              WHERE ip.trackedItem in :trackedItems
              AND ip.timestamp > :since
      """)
  List<ItemPriceEntity> findLatestPricesForTrackedItems(List<TrackedItemEntity> trackedItems,
                                                        Instant since);
}
