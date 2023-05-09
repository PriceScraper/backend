package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.entity.shops.ItemPriceEntity;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.jpa.ItemPriceJpaRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of JPA repository.
 */
@Repository
@AllArgsConstructor
public class ItemPriceRepositoryImpl implements ItemPriceRepository {
  private final ItemPriceJpaRepository itemPriceJpaRepository;
  private final ModelMapper modelMapper;

  /**
   * Save entity.
   */
  @Override
  public ItemPrice save(ItemPrice itemPrice) {
    ItemPriceEntity savedEntity =
        itemPriceJpaRepository.save(modelMapper.map(itemPrice, ItemPriceEntity.class));
    return modelMapper.map(savedEntity, ItemPrice.class);
  }
}
