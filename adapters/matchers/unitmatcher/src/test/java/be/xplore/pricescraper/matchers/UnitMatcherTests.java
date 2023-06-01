package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.UnitType;
import org.junit.jupiter.api.Test;

public class UnitMatcherTests {

  private static final Item itemA =
      new Item("Pizza salami", "", 1, UnitType.KG, 1, "", null);
  private static final Item itemB =
      new Item("Pizza salami", "", 1, UnitType.G, 1000, "", null);
  private static final Item itemC =
      new Item("Pizza salami", "", 1, UnitType.G, 100, "", null);
  private static final Item itemD =
      new Item("Pizza salami", "", 1, UnitType.ML, 100, "", null);

  @Test
  void shouldMatch() {
    Matcher matcher = new UnitMatcher();
    matcher.addItems(itemA, itemB);
    assertThat(matcher.getMatchProbabilityInPercentage() > 0.9).isTrue();
  }

  @Test
  void differentAmountShouldNotMatch() {
    Matcher matcher = new UnitMatcher();
    matcher.addItems(itemA, itemC);
    assertThat(matcher.getMatchProbabilityInPercentage() > 0.9).isFalse();
  }

  @Test
  void differentCategoryShouldNotMatch() {
    Matcher matcher = new UnitMatcher();
    matcher.addItems(itemA, itemD);
    assertThat(matcher.getMatchProbabilityInPercentage() > 0.9).isFalse();
  }

}
