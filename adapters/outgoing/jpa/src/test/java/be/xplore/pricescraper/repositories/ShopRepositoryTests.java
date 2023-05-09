package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.repositories.implementations.ShopRepositoryImpl;
import be.xplore.pricescraper.utils.ModelMapperUtil;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ShopRepositoryImpl.class, ModelMapperUtil.class})
class ShopRepositoryTests {
  @Autowired
  ShopRepositoryImpl shopRepository;

  @Test
  void findByUrlShouldReturnShop() {
    Shop shop = new Shop(1, "test", "test");
    shopRepository.save(shop);
    Optional<Shop> shopFromRepository = shopRepository.findByUrl("test");
    assertThat(shopFromRepository).isPresent();
  }

  @Test
  void shopShouldSave() {
    Shop shop = new Shop(1, "test", "test");
    Shop savedShop = shopRepository.save(shop);
    assertThat(savedShop.getUrl()).isEqualTo("test");
  }
}
