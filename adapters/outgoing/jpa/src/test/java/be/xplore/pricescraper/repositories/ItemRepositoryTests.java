package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.repositories.implementations.ItemRepositoryImpl;
import be.xplore.pricescraper.repositories.implementations.TrackedItemRepositoryImpl;
import be.xplore.pricescraper.utils.ModelMapperUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@Import({ItemRepositoryImpl.class, TrackedItemRepositoryImpl.class, ModelMapperUtil.class})
class ItemRepositoryTests {
  @Autowired
  ItemRepositoryImpl itemRepository;
  @Autowired
  TrackedItemRepositoryImpl trackedItemRepository;
  @PersistenceContext
  EntityManager entityManager;
  @Autowired
  DataSource dataSource;

  @BeforeAll
  void setup() throws InterruptedException, SQLException {
    try (Connection con = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(con, new ClassPathResource("sql/item_test_data.sql"));
    }
    entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    SearchSession searchSession = Search.session(entityManager);
    searchSession.massIndexer().startAndWait();
  }


  @Test
  void itemShouldHave2TrackedItems() {
    Optional<Item> item = itemRepository.findItemWithTrackedItemsById(101);
    assertThat(item).isPresent();
    assertThat(item.get().getTrackedItems()).hasSize(2);
  }

  @Test
  void trackedItemsShouldHaveLatestPrices() {
    var item =
        itemRepository.findItemWithTrackedItemsById(101);
    var itemPrices =
        itemRepository.findLatestPricesForTrackedItems(item.get().getTrackedItems());
    var dayOfMonthForTimestamps =
        itemPrices.stream().map(
                itemPrice -> itemPrice.getTimestamp().atZone(ZoneId.of("Europe/Brussels"))
                    .getDayOfMonth())
            .toList();
    var expectedDays = List.of(22, 21);
    assertThat(dayOfMonthForTimestamps).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(expectedDays);
    assertThat(itemPrices).hasSize(2);
  }

  @Test
  void searchShouldHaveResult() {
    var items = itemRepository.findItemByNameWithFuzzySearchAndLimit("pizza", 1);
    assertThat(items).hasSize(1);
  }

  @Test
  void resultsShouldHaveTrackedItems() {
    var items = itemRepository.findAll();
    assertThat(items.get(0).getTrackedItems()).isNotNull();
  }

  @Test
  void resultShouldHave2Items() {
    var items = itemRepository.findItemsByNameLike("PIzZa");
    assertThat(items).hasSize(2);
  }

  @Test
  void resultShouldHavePrices() {
    TrackedItem trackedItem =
        trackedItemRepository.findAll(Pageable.ofSize(1)).get().findFirst().orElseThrow();
    var items = itemRepository.findLatestPricesForTrackedItems(List.of(trackedItem),
        LocalDateTime.of(2022, 1, 1, 1, 1));
    assertThat(items).isNotEmpty();
  }

  @Test
  void testDto() {
    var searchDto = new ItemScraperSearch("title", "url");
    assertNotNull(searchDto);
    assertNotNull(searchDto.title());
    assertNotNull(searchDto.url());
  }

}
