package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link Item}.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, CustomItemRepository {

}
