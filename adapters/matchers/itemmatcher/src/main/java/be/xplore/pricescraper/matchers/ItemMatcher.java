package be.xplore.pricescraper.matchers;


import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.utils.matchers.Matcher;

/**
 * This is the abstract definition for an ItemMatcher that matches equality of {@link Item}.
 */
public abstract class ItemMatcher implements Matcher {

  private final Item itemA;
  private final Item itemB;

  protected ItemMatcher(Item itemA, Item itemB) {
    this.itemA = itemA;
    this.itemB = itemB;
  }

  /**
   * Normalizes score of matching algorithm to a probability percentage that the items are matching.
   * The range consists of predicted algorithm results, numbers outside the range get rounded.
   */
  protected double normalizeScoreToPercentageGivenRange(int score, int min, int max) {
    if (score > max) {
      score = max;
    }
    if (score < min) {
      score = min;
    }
    return 1 - (double) (score - min) / (max - min);
  }

  protected Item getItemA() {
    return itemA;
  }

  protected Item getItemB() {
    return itemB;
  }

}
