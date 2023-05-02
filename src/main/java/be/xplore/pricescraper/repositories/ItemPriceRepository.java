package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.ItemPrice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository.
 */
public interface ItemPriceRepository extends JpaRepository<ItemPrice, Integer> {
}
