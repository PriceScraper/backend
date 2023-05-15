package be.xplore.pricescraper.matchers.combiners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.domain.shops.Item;
import org.junit.jupiter.api.Test;

class WeightedItemMatcherCombinerTests {

  private static final WeightedItemMatcherCombiner combiner =
      new WeightedItemMatcherCombiner(0.7);

  static {
    combiner.addMatcher(new MockItemMatcher(new Item(), new Item()), 0.9);
    combiner.addMatcher(new MockItemMatcher(new Item(), new Item()), 0.1);
  }

  @Test
  void combinerShouldHaveMatchPercentage() {
    double percentage = combiner.getMatchProbabilityInPercentage();
    assertThat(percentage).isLessThan(1).isPositive();
  }

  @Test
  void shouldMatch() {
    assertThat(combiner.isMatching()).isTrue();
  }

  @Test
  void combinerShouldThrow() {
    Item itemA = new Item();
    Item itemB = new Item();
    WeightedItemMatcherCombiner otherCombiner =
        new WeightedItemMatcherCombiner(0.7);
    otherCombiner.addMatcher(new MockItemMatcher(itemA, itemB), 0.5);
    assertThatThrownBy(
        () -> otherCombiner.addMatcher(new MockItemMatcher(itemA, itemB), 0.7)).isInstanceOf(
        IllegalArgumentException.class);

  }

}
