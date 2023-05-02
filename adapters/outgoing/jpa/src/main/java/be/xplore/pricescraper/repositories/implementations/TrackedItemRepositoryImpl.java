package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.entity.shops.TrackedItemEntity;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import be.xplore.pricescraper.repositories.jpa.TrackedItemJpaRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Implementation of JPA repository.
 */
@Repository
@AllArgsConstructor
public class TrackedItemRepositoryImpl implements TrackedItemRepository {
  private final TrackedItemJpaRepository jpaRepository;
  private final ModelMapper modelMapper;

  /**
   * Returns true if {@link TrackedItem} exists by url and shop id.
   */
  @Override
  public boolean existsByUrlIgnoreCaseAndShopId(String url, int id) {
    return jpaRepository.existsByUrlIgnoreCaseAndShop_Id(url, id);
  }

  /**
   * Saves a {@link TrackedItem}.
   */
  @Override
  public TrackedItem save(TrackedItem trackedItem) {
    var entity = modelMapper.map(trackedItem, TrackedItemEntity.class);
    entity = jpaRepository.save(entity);
    return modelMapper.map(entity, TrackedItem.class);
  }

  /**
   * Returns count of {@link TrackedItem}.
   */
  @Override
  public long count() {
    return jpaRepository.count();
  }

  /**
   * Finds all {@link TrackedItem}.
   */
  @Override
  public Page<TrackedItem> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable)
        .map(entity -> modelMapper.map(entity, TrackedItem.class));
  }
}
