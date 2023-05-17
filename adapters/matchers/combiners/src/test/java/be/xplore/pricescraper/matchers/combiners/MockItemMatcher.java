package be.xplore.pricescraper.matchers.combiners;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.matchers.ItemMatcher;

public class MockItemMatcher extends ItemMatcher {
  protected MockItemMatcher(Item itemA,
                            Item itemB) {
    super(itemA, itemB);
  }

  @Override
  public double getMatchProbabilityInPercentage() {
    return normalizeScoreToPercentageGivenRange(75, 0, 300);
  }

  @Override
  public boolean isMatching() {
    return false;
  }
}
