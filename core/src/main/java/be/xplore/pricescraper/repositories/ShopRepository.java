package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Shop;
import java.util.Optional;

/**
 * Repository for {@link Shop}.
 */
public interface ShopRepository {
  Optional<Shop> findByUrl(String scraperDomain);

  Shop save(Shop shop);
}
