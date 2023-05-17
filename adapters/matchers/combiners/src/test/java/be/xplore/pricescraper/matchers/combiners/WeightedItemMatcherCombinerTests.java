package be.xplore.pricescraper.matchers.combiners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemUnit;
import org.junit.jupiter.api.Test;

class WeightedItemMatcherCombinerTests {

  private static final WeightedItemMatcherCombiner combiner =
      new WeightedItemMatcherCombiner();

  private static final Item itemA = new Item("", "", 1,
      new ItemUnit(ItemUnit.UnitType.KILOGRAMS, 1), "");
  private static final Item itemB = new Item("", "", 1,
      new ItemUnit(ItemUnit.UnitType.KILOGRAMS, 1), "");

  static {
    combiner.addMatcher(new MockItemMatcher(), 0.9);
    combiner.addMatcher(new MockItemMatcher(), 0.1);

  }

  @Test
  void combinerShouldHaveMatchPercentage() {
    combiner.addItems(itemA, itemB);
    double percentage = combiner.getMatchProbabilityInPercentage();
    assertThat(percentage).isPositive();
  }

  @Test
  void shouldMatch() {
    combiner.addItems(itemA, itemB);
    assertThat(combiner.isMatching()).isTrue();
  }

  @Test
  void combinerShouldThrow() {
    WeightedItemMatcherCombiner otherCombiner =
        new WeightedItemMatcherCombiner();
    otherCombiner.addMatcher(new MockItemMatcher(), 0.5);
    assertThatThrownBy(
        () -> otherCombiner.addMatcher(new MockItemMatcher(), 0.7)).isInstanceOf(
        IllegalArgumentException.class);

  }

}
