package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemUnit;
import be.xplore.pricescraper.exceptions.MatcherNotInitializedException;
import org.junit.jupiter.api.Test;

class TitleMatcherTests {

  private static final Item itemA =
      new Item(1, "Halfvolle melk AH", "", 1, new ItemUnit(), "", null);
  private static final Item itemB =
      new Item(1, "Halfvolle melk", "", 1, new ItemUnit(), "", null);
  private static final Item itemC =
      new Item(1, "Volle melk Simple", "", 1, new ItemUnit(), "", null);

  @Test
  void shouldMatchProducts() {
    TitleMatcher titleMatcher = new TitleMatcher();
    titleMatcher.addItems(itemA, itemB);
    boolean matched =
        titleMatcher.isMatching();
    assertThat(matched).isTrue();
  }

  @Test
  void shouldNotBeInitialized() {
    TitleMatcher titleMatcher = new TitleMatcher();
    assertThatThrownBy(titleMatcher::isMatching).isInstanceOf(
        MatcherNotInitializedException.class);
  }

  @Test
  void shouldNotMatchProducts() {
    TitleMatcher titleMatcher = new TitleMatcher();
    titleMatcher.addItems(itemA, itemC);
    boolean notMatched =
        titleMatcher.isMatching();
    assertThat(notMatched).isFalse();
  }

}
