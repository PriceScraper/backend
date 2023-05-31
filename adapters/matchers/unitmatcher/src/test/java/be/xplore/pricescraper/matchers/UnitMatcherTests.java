package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.UnitType;
import org.junit.jupiter.api.Test;

public class UnitMatcherTests {

  private static final Item itemA =
      new Item(1, "Pizza salami", "", 1, UnitType.kg, 1, "", null);
  private static final Item itemB =
      new Item(1, "Pizza salami", "", 1, UnitType.g, 1000, "", null);
  private static final Item itemC =
      new Item(1, "Pizza salami", "", 1, UnitType.g, 100, "", null);
  private static final Item itemD =
      new Item(1, "Pizza salami", "", 1, UnitType.ml, 100, "", null);

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
    assertThat(matcher.isMatching()).isFalse();
  }

  @Test
  void differentCategoryShouldNotMatch() {
    Matcher matcher = new UnitMatcher();
    matcher.addItems(itemA, itemD);
    assertThat(matcher.isMatching()).isFalse();
  }

}
