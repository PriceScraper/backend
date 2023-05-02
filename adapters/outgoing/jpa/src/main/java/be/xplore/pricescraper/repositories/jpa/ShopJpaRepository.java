package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.shops.ShopEntity;
import be.xplore.pricescraper.repositories.ShopRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository implementation of {@link ShopRepository}.
 */
@Repository
public interface ShopJpaRepository extends JpaRepository<ShopEntity, Integer> {

  Optional<ShopEntity> findByUrl(String url);

}
