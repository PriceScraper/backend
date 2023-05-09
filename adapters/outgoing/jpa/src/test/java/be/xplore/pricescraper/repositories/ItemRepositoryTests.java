package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.repositories.implementations.ItemRepositoryImpl;
import be.xplore.pricescraper.utils.ModelMapperUtil;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/sql/item_test_data.sql"})
@DataJpaTest
@Import({ItemRepositoryImpl.class, ModelMapperUtil.class})
class ItemRepositoryTests {
  @Autowired
  ItemRepositoryImpl itemRepository;

  @Test
  void itemShouldHave2TrackedItems() {
    Optional<Item> item = itemRepository.findItemWithTrackedItemsById(1);
    assertThat(item.isPresent()).isTrue();
    assertThat(item.get().getTrackedItems()).hasSize(2);
  }

  @Test
  void trackedItemsShouldHaveLatestPrices() {
    Optional<Item> item =
        itemRepository.findItemWithTrackedItemsById(1);
    List<ItemPrice> itemPrices =
        itemRepository.findLatestPricesForTrackedItems(item.get().getTrackedItems());
    List<Integer> dayOfMonthForTimestamps =
        itemPrices.stream().map(
                itemPrice -> itemPrice.getTimestamp().atZone(ZoneId.of("Europe/Brussels"))
                    .getDayOfMonth())
            .toList();
    List<Integer> expectedDays = List.of(22, 21);
    assertThat(dayOfMonthForTimestamps).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(expectedDays);
    assertThat(itemPrices).hasSize(2);
  }

  @Test
  void resultShouldHave2Items() {
    List<ItemSearchDto> items =
        itemRepository.findItemsByNameLike("PIzZa");
    assertThat(items).hasSize(2);
  }

}
