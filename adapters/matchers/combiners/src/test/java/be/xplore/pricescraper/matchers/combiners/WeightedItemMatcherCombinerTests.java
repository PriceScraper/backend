package be.xplore.pricescraper.matchers.combiners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.UnitType;
import be.xplore.pricescraper.exceptions.MatchException;
import org.junit.jupiter.api.Test;

class WeightedItemMatcherCombinerTests {

  private static final WeightedItemMatcherCombiner combiner =
      new WeightedItemMatcherCombiner();

  private static final Item itemA = new Item("aaaaa", "", 1,
      UnitType.KG, 1, "", null);
  private static final Item itemB = new Item("aaaab", "", 1,
      UnitType.KG, 1, "", null);

  private static final Item itemC = new Item("", "", 1,
      UnitType.KG, 1, "", null);

  @Test
  void combinerShouldHaveMatchPercentage() {
    combiner.addMatcher(new MockItemMatcher());
    combiner.addItems(itemA, itemB);
    combiner.addWeightToMatcher(MockItemMatcher.class, 1.0);

    double percentage = combiner.getMatchProbabilityInPercentage();
    assertThat(percentage).isPositive();
  }

  @Test
  void shouldMatch() {
    combiner.addMatcher(new MockItemMatcher());
    combiner.addItems(itemA, itemB);
    combiner.addWeightToMatcher(MockItemMatcher.class, 1.0);
    assertThat(combiner.isMatching()).isTrue();
  }

  @Test
  void matchingShouldFail() {
    combiner.addMatcher(new MockItemMatcher());
    combiner.addItems(itemA, itemC);
    combiner.addWeightToMatcher(MockItemMatcher.class, 1.0);
    assertThatThrownBy(combiner::isMatching).isInstanceOf(MatchException.class);
  }

}
