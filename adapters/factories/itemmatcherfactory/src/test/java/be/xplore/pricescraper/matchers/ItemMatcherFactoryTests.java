package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemUnit;
import be.xplore.pricescraper.utils.matchers.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ItemMatcherFactoryImpl.class})
class ItemMatcherFactoryTests {

  @Autowired
  ItemMatcherFactoryImpl factory;

  private final static Item itemA = new Item("a",
      "https://www.aldi.nl/content/dam/aldi/netherlands/aldi_merken/9453-01.png",
      1,
      new ItemUnit(ItemUnit.UnitType.KILOGRAMS, 1), "");
  private final static Item itemB =
      new Item("b",
          "https://be.openfoodfacts.org/images/products/400/172/481/9004/front_de.39.full.jpg",
          1,
          new ItemUnit(ItemUnit.UnitType.KILOGRAMS, 1), "");

  @Test
  void factoryShouldReturnMatcher() {
    Matcher matcher = factory.makeByNameAndItems("ingredient", itemA, itemB);
    assertThat(matcher).isNotNull();
  }

  @Test
  void factoryShouldThrow() {
    assertThatThrownBy(
        () -> factory.makeByNameAndItems("doesnotexist", itemA, itemB)).isInstanceOf(
        IllegalArgumentException.class);
  }

}
