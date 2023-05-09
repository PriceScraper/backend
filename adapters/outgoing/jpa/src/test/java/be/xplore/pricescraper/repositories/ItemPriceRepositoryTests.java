package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.repositories.implementations.ItemPriceRepositoryImpl;
import be.xplore.pricescraper.utils.ModelMapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ItemPriceRepositoryImpl.class, ModelMapperUtil.class})
class ItemPriceRepositoryTests {
  @Autowired
  ItemPriceRepositoryImpl itemPriceRepository;

  @Test
  void itemPriceShouldHaveIdAfterSave() {
    ItemPrice itemPrice = new ItemPrice();
    itemPrice = itemPriceRepository.save(itemPrice);
    assertThat(itemPrice.getId()).isPositive();
  }
}
