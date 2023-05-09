package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.repositories.implementations.ShopRepositoryImpl;
import be.xplore.pricescraper.repositories.implementations.TrackedItemRepositoryImpl;
import be.xplore.pricescraper.utils.ModelMapperUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TrackedItemRepositoryImpl.class, ShopRepositoryImpl.class, ModelMapperUtil.class})
public class TrackedItemRepositoryTests {
  @Autowired
  TrackedItemRepositoryImpl trackedItemRepository;
  @Autowired
  ShopRepositoryImpl shopRepository;

  @BeforeAll
  void setup() {
    Shop shop = shopRepository.save(new Shop());
    TrackedItem testItem = new TrackedItem("test", shop, null);
    trackedItemRepository.save(testItem);
  }

  @Test
  void trackedItemShouldExist() {
    boolean exists = trackedItemRepository.existsByUrlIgnoreCaseAndShopId("test", 1);
    assertThat(exists).isTrue();
  }

  @Test
  void countShouldBePositive() {
    long count = trackedItemRepository.count();
    assertThat(count).isPositive();
  }

  @Test
  void findAllShouldReturnTrackedItem() {
    Page<TrackedItem> trackedItemsPage = trackedItemRepository.findAll(Pageable.ofSize(10));
    assertThat(trackedItemsPage.getTotalElements()).isPositive();
  }

}
