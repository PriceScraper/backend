package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.entity.shops.ShopEntity;
import be.xplore.pricescraper.repositories.ShopRepository;
import be.xplore.pricescraper.repositories.jpa.ShopJpaRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of JPA repository.
 */
@Repository
@AllArgsConstructor
public class ShopRepositoryImpl implements ShopRepository {
  private final ShopJpaRepository shopJpaRepository;
  private final ModelMapper modelMapper;

  /**
   * Finds a {@link Shop} by url.
   */
  @Override
  public Optional<Shop> findByUrl(String scraperDomain) {
    Optional<ShopEntity> entity = shopJpaRepository.findByUrl(scraperDomain);
    return entity.map(e -> modelMapper.map(e, Shop.class));
  }

  /**
   * Saves a {@link Shop}.
   */
  @Override
  public Shop save(Shop shop) {
    ShopEntity savedEntity = shopJpaRepository.save(modelMapper.map(shop, ShopEntity.class));
    return modelMapper.map(savedEntity, Shop.class);
  }
}
