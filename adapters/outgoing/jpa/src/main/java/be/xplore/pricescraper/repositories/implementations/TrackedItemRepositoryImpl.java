package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.entity.shops.ItemEntity;
import be.xplore.pricescraper.entity.shops.ItemPriceEntity;
import be.xplore.pricescraper.entity.shops.TrackedItemEntity;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import be.xplore.pricescraper.repositories.jpa.ItemJpaRepository;
import be.xplore.pricescraper.repositories.jpa.ItemPriceJpaRepository;
import be.xplore.pricescraper.repositories.jpa.ShopJpaRepository;
import be.xplore.pricescraper.repositories.jpa.TrackedItemJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
  private final ShopJpaRepository shopRepository;
  private final ItemJpaRepository itemRepository;
  private final ItemPriceJpaRepository itemPriceRepository;
  private final ModelMapper modelMapper;

  /**
   * Returns true if {@link TrackedItem} exists by url and shop id.
   */
  @Override
  public boolean existsByUrlIgnoreCaseAndShopId(String url, int id) {
    return jpaRepository.existsByUrlIgnoreCaseAndShop_Id(url, id);
  }

  @Override
  public Optional<TrackedItem> findByUrlIgnoreCaseAndShopId(String url, int id) {
    return jpaRepository.findByUrlIgnoreCaseAndShopId(url, id)
        .map(e -> modelMapper.map(e, TrackedItem.class));
  }

  /**
   * Saves a {@link TrackedItem}.
   */
  @Override
  public TrackedItem save(TrackedItem trackedItem) {
    ItemEntity item = trackedItem.getItem() != null
        ?
        itemRepository.getReferenceById(trackedItem.getItem().getId()) : null;
    List<ItemPriceEntity> prices =
        trackedItem.getItemPrices() != null ? trackedItem.getItemPrices().stream()
            .map((ip) -> itemPriceRepository.getReferenceById(ip.getId())).toList() :
            new ArrayList<>();
    TrackedItemEntity entity =
        new TrackedItemEntity(trackedItem.getUrl(),
            shopRepository.getReferenceById(trackedItem.getShop().getId()),
            item,
            prices,
            trackedItem.getLastAttempt());
    entity = jpaRepository.saveAndFlush(entity);
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

  @Override
  public boolean existsByUrlIgnoreCase(String url) {
    return jpaRepository.existsByUrlIgnoreCase(url);
  }
}
