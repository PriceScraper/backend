package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Implementation for {@link CustomItemRepository}.
 * This implementation adds specific queries like finding items
 * by name with only their last price
 */
@Component
public class CustomItemRepositoryImpl implements CustomItemRepository {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Find items including tracked items and their last prices.
   *
   * @param name name of {@link Item}
   * @return list of {@link Item}
   */
  public List<ItemSearchDto> findItemsByNameLike(String name) {
    return entityManager.createQuery("""
                    SELECT NEW be.xplore.pricescraper.dtos.ItemSearchDto(i.id, i.name, i.image)
                     from Item i
                    WHERE LOWER(i.name) LIKE CONCAT('%', LOWER(:name), '%')
            """, ItemSearchDto.class)
        .setParameter("name", name).getResultList();

  }

  /**
   * Find items including tracked items and their last prices.
   *
   * @param id id of {@link Item}
   * @return {@link Item}
   */
  public Item findItemWithTrackedItemsById(int id) {
    return entityManager.createQuery("""
                    SELECT i from Item i
                    LEFT JOIN FETCH i.trackedItems
                    WHERE i.id = :id
            """, Item.class)
        .setParameter("id", id).getSingleResult();
  }

  /**
   * Fetch a single ItemPrice per TrackedItem to reduce data overhead.
   */
  public List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems) {
    return entityManager.createQuery("""
                    SELECT DISTINCT ip from ItemPrice ip
                    WHERE ip.trackedItem in :trackedItems
                    AND ip.timestamp = (SELECT MAX(ip2.timestamp)
                     FROM ItemPrice ip2
                    WHERE ip2.trackedItem = ip.trackedItem)
            """, ItemPrice.class)
        .setParameter("trackedItems", trackedItems).getResultList();
  }

}
