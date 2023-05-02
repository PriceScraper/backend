package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Shop;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Shop}.
 */
@Repository
public interface ShopRepository {
  Optional<Shop> findByUrl(String scraperDomain);

  Shop save(Shop shop);
}
