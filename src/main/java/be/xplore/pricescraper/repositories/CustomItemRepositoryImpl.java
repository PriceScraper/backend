package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
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
  public List<Item> findItemsWithTrackedItemsAndLastPriceByNameLike(String name) {
    List<Item> items = fetchItemsLikeName(name);
    List<ItemPrice> itemPrices = fetchPricesForTrackedItems(items);
    assignPricesToTrackedItemsOfItems(items, itemPrices);
    return items;
  }

  private List<Item> fetchItemsLikeName(String name) {
    return entityManager.createQuery("""
                    SELECT i from Item i
                    LEFT JOIN FETCH i.trackedItems
                    WHERE LOWER(i.name) LIKE CONCAT('%', LOWER(:name), '%')
            """, Item.class)
        .setParameter("name", name).getResultList();
  }

  //fetch a single ItemPrice to reduce processing overhead, the mapping of itemprices to
  //trackeditems will occur in memory but this approach outweighs possibly fetching thousands of
  //prices when there will only ever be a few trackeditems
  private List<ItemPrice> fetchPricesForTrackedItems(List<Item> items) {
    //get flat list of all tracked items
    List<TrackedItem> trackedItems = flattenTrackedItems(items);
    return entityManager.createQuery("""
                    SELECT DISTINCT ip from ItemPrice ip
                    WHERE ip.trackedItem in :trackedItems
                    GROUP BY ip.trackedItem, ip.id
                    HAVING ip.timestamp = max(ip.timestamp)
            """, ItemPrice.class)
        .setParameter("trackedItems", trackedItems).getResultList();
  }

  private List<TrackedItem> flattenTrackedItems(List<Item> items) {
    List<TrackedItem> trackedItems = new ArrayList<>();
    items.stream().map(Item::getTrackedItems).toList().forEach(trackedItems::addAll);
    return trackedItems;
  }

  //mapping of itemprices in memory
  private void assignPricesToTrackedItemsOfItems(List<Item> items, List<ItemPrice> itemPrices) {
    itemPrices.forEach(itemPrice -> items.forEach(item -> item.getTrackedItems().stream()
        .filter(trackedItem -> trackedItem.getUrl().equals(itemPrice.getTrackedItem().getUrl()))
        .findFirst().ifPresent(trackedItem -> trackedItem.setItemPrices(List.of(itemPrice)))));
  }

}
