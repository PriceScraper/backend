package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.entity.shops.ItemEntity;
import be.xplore.pricescraper.entity.shops.ItemPriceEntity;
import be.xplore.pricescraper.entity.shops.ItemSearchEntity;
import be.xplore.pricescraper.entity.shops.TrackedItemEntity;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.jpa.ItemJpaRepository;
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
  private final ModelMapper modelMapper;

  /**
   * Find by name like %name%.
   */
  @Override
  public List<ItemSearchDto> findItemsByNameLike(String name) {
    List<ItemSearchEntity> entities = itemJpaRepository.findByNameContainsIgnoreCase(name);
    return entities.stream().map(e -> new ItemSearchDto(e.id(), e.name(), e.image())).toList();
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
   * Save entity.
   */
  @Override
  public Item save(Item item) {
    ItemEntity savedEntity = itemJpaRepository.save(modelMapper.map(item, ItemEntity.class));
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
