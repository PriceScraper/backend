package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Shop;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ShopRepository.
 */
public interface ShopRepository extends JpaRepository<Shop, Integer> {
  Optional<Shop> findByUrl(String scraperDomain);
}
