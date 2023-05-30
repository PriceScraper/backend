package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.shops.TrackedItemEntity;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository implementation of {@link TrackedItemRepository}.
 */
@Repository
public interface TrackedItemJpaRepository
    extends JpaRepository<TrackedItemEntity, String> {
  boolean existsByUrlIgnoreCaseAndShop_Id(String url, int id);

  Optional<TrackedItemEntity> findByUrlIgnoreCaseAndShopId(String url, int id);

  long count();

  boolean existsByUrlIgnoreCase(String url);
}
