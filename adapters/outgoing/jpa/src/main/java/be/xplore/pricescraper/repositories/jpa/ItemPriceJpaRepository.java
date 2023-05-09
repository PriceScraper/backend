package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.shops.ItemPriceEntity;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository implementation of {@link ItemPriceRepository}.
 */
@Repository
public interface ItemPriceJpaRepository
    extends JpaRepository<ItemPriceEntity, Integer> {
}
