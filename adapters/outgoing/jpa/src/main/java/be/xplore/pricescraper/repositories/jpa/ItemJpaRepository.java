package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.shops.ItemEntity;
import be.xplore.pricescraper.entity.shops.ItemPriceEntity;
import be.xplore.pricescraper.entity.shops.ItemSearchEntity;
import be.xplore.pricescraper.entity.shops.TrackedItemEntity;
import be.xplore.pricescraper.repositories.ItemRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * JPA repository implementation of {@link ItemRepository}.
 */
@Repository
public interface ItemJpaRepository
    extends JpaRepository<ItemEntity, Integer> {
  List<ItemSearchEntity> findByNameContainsIgnoreCase(String name);


  @Query("""
              SELECT i from Item i
              LEFT JOIN FETCH i.trackedItems
              WHERE i.id = :id
      """)
  ItemEntity findItemWithTrackedItemsById(int id);

  @Query("""
              SELECT DISTINCT ip from ItemPrice ip
              WHERE ip.trackedItem in :trackedItems
              AND ip.timestamp = (SELECT MAX(ip2.timestamp)
               FROM ItemPrice ip2
              WHERE ip2.trackedItem = ip.trackedItem)
      """)
  List<ItemPriceEntity> findLatestPricesForTrackedItems(List<TrackedItemEntity> trackedItems);
}
