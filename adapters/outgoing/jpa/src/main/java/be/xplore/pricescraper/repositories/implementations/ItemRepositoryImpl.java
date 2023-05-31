package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.entity.shops.ItemEntity;
import be.xplore.pricescraper.entity.shops.ItemPriceEntity;
import be.xplore.pricescraper.entity.shops.TrackedItemEntity;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.jpa.ItemJpaRepository;
import be.xplore.pricescraper.repositories.jpa.TrackedItemJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of JPA repository.
 */
@Repository
@AllArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
  private final ItemJpaRepository itemJpaRepository;
  private final TrackedItemJpaRepository trackedItemRepository;
  private final ModelMapper modelMapper;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Item> findAll() {
    List<ItemEntity> itemEntities = itemJpaRepository.findAllWithTrackedItems();
    return itemEntities.stream().map(e -> modelMapper.map(e, Item.class)).toList();
  }

  @Override
  @Transactional
  public List<ItemSearchDto> findItemByNameWithFuzzySearchAndLimit(String nameQuery, int limit) {
    SearchSession searchSession = Search.session(entityManager);
    SearchResult<ItemEntity> result = searchSession.search(ItemEntity.class)
        .where(f -> f.match().field("name")
            .matching(nameQuery).analyzer("itemNameQuery"))
        .fetch(limit);
    List<ItemEntity> hits = result.hits().stream().distinct().toList();
    return mapToItemSearchDtos(hits);
  }

  private static List<ItemSearchDto> mapToItemSearchDtos(List<ItemEntity> entities) {
    return entities.stream().map(ie -> new ItemSearchDto(ie.getId(), ie.getName(), ie.getImage()))
        .toList();
  }

  /**
   * Find by id with JOIN on tracked items.
   */
  @Override
  public Optional<Item> findItemWithTrackedItemsById(int id) {
    Optional<ItemEntity> entity = itemJpaRepository.findItemWithTrackedItemsById(id);
    return entity.map(userEntity -> modelMapper.map(userEntity, Item.class));
  }

  /**
   * Find tracked items with latest price.
   */
  @Override
  public List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems) {
    var trackedEntities =
        trackedItems.stream().map(e -> modelMapper.map(e, TrackedItemEntity.class)).toList();
    List<ItemPriceEntity> entities =
        itemJpaRepository.findLatestPricesForTrackedItems(trackedEntities);
    return entities.stream().map(e -> modelMapper.map(e, ItemPrice.class)).toList();
  }

  /**
   * Find tracked items with latest price.
   */
  @Override
  public List<ItemPrice> findLatestPricesForTrackedItems(List<TrackedItem> trackedItems,
                                                         LocalDateTime since) {
    var trackedEntities =
        trackedItems.stream().map(e -> modelMapper.map(e, TrackedItemEntity.class)).toList();
    List<ItemPriceEntity> entities =
        itemJpaRepository.findLatestPricesForTrackedItems(trackedEntities,
            since.toInstant(ZoneOffset.UTC));
    return entities.stream().map(e -> modelMapper.map(e, ItemPrice.class)).toList();
  }

  /**
   * Save entity.
   */
  @Override
  public Item save(Item item) {
    List<TrackedItemEntity> trackedItems = item.getTrackedItems() != null
        ? item.getTrackedItems().stream()
        .map((tr) -> trackedItemRepository.getReferenceById(tr.getUrl())).toList()
        : null;

    var itemToSave =
        new ItemEntity(item.getId(), item.getName(), item.getImage(), item.getQuantity(),
            item.getType(), item.getAmount(),
            item.getIngredients(),
            trackedItems);
    var savedEntity = itemJpaRepository.saveAndFlush(itemToSave);
    return modelMapper.map(savedEntity, Item.class);
  }

  /**
   * Find by id.
   */
  @Override
  public Optional<Item> findById(int id) {
    Optional<ItemEntity> entity = itemJpaRepository.findById(id);
    return entity.map(userEntity -> modelMapper.map(userEntity, Item.class));
  }
}
