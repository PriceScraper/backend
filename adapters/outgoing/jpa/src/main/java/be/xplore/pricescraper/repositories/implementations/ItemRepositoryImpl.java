package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.entity.shops.ItemEntity;
import be.xplore.pricescraper.entity.shops.ItemPriceEntity;
import be.xplore.pricescraper.entity.shops.ItemUnitEntity;
import be.xplore.pricescraper.entity.shops.TrackedItemEntity;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.jpa.ItemJpaRepository;
import be.xplore.pricescraper.repositories.jpa.TrackedItemJpaRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
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

  /**
   * Find by name like %name%.
   */
  @Override
  public List<ItemSearchDto> findItemsByNameLike(String name) {
    var entities = itemJpaRepository.findByNameContainsIgnoreCase(name.toLowerCase());
    return entities.stream()
        .map(e -> modelMapper.map(e, ItemSearchDto.class))
        .filter(e -> e.getId() > 0)
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
    ItemUnitEntity itemUnit = item.getUnit() != null
        ?
        new ItemUnitEntity(item.getUnit().getType(), item.getUnit().getContent()) : null;
    List<TrackedItemEntity> trackedItems = item.getTrackedItems() != null
        ? item.getTrackedItems().stream()
        .map((tr) -> trackedItemRepository.getReferenceById(tr.getUrl())).toList()
        : null;

    ItemEntity itemToSave =
        new ItemEntity(item.getId(), item.getName(), item.getImage(), item.getQuantity(),
            itemUnit,
            item.getIngredients(),
            trackedItems);
    var savedEntity = itemJpaRepository.saveAndFlush(itemToSave);
    return modelMapper.map(savedEntity, Item.class);
  }

  /**
   * Find by key.
   */
  @Override
  public Optional<Item> findById(int id) {
    Optional<ItemEntity> entity = itemJpaRepository.findById(id);
    return entity.map(userEntity -> modelMapper.map(userEntity, Item.class));
  }
}
