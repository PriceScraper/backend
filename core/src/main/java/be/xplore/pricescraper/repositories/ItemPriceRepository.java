package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.ItemPrice;

/**
 * Repository for {@link ItemPrice}.
 */
public interface ItemPriceRepository {
  ItemPrice save(ItemPrice itemPrice);
}
