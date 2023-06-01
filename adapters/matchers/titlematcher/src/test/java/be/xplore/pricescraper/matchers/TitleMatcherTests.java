package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.UnitType;
import be.xplore.pricescraper.exceptions.MatcherNotInitializedException;
import org.junit.jupiter.api.Test;

class TitleMatcherTests {

  private static final Item itemA =
      new Item(1, "Halfvolle melk AH", "", 1, UnitType.ML, 300, "", null);
  private static final Item itemB =
      new Item(1, "Halfvolle melk", "", 1, UnitType.ML, 300, "", null);
  private static final Item itemC =
      new Item(1, "Volle melk Simple", "", 1, UnitType.ML, 300, "", null);

  @Test
  void shouldMatchProducts() {
    TitleMatcher titleMatcher = new TitleMatcher();
    titleMatcher.addItems(itemA, itemB);
    boolean matched =
        titleMatcher.getMatchProbabilityInPercentage() > 0.7;
    assertThat(matched).isTrue();
  }

  @Test
  void shouldNotBeInitialized() {
    TitleMatcher titleMatcher = new TitleMatcher();
    assertThatThrownBy(titleMatcher::getMatchProbabilityInPercentage).isInstanceOf(
        MatcherNotInitializedException.class);
  }

  @Test
  void shouldNotMatchProducts() {
    TitleMatcher titleMatcher = new TitleMatcher();
    titleMatcher.addItems(itemA, itemC);
    boolean notMatched =
        titleMatcher.getMatchProbabilityInPercentage() > 0.7;
    assertThat(notMatched).isFalse();
  }

  @Test
  void shouldBeAbleToMatch() {
    TitleMatcher titleMatcher = new TitleMatcher();
    titleMatcher.addItems(itemA, itemC);
    boolean matchingPossible =
        titleMatcher.matchingIsPossible();
    assertThat(matchingPossible).isTrue();
  }

}
