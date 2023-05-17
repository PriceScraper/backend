package be.xplore.pricescraper.matchers;


import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.utils.matchers.Matcher;

/**
 * This is the abstract definition for an ItemMatcher that matches equality of {@link Item}.
 */
public abstract class ItemMatcher implements Matcher {

  private boolean initialized = false;

  private Item itemA = null;
  private Item itemB = null;

  @Override
  public void addItems(Item a, Item b) {
    itemA = a;
    itemB = b;
    initialized = true;
  }

  protected void setInitialized(boolean initialized) {
    this.initialized = initialized;
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

  protected boolean isInitialized() {
    return initialized;
  }

}
