package be.xplore.pricescraper.matchers.combiners;

import be.xplore.pricescraper.matchers.ItemMatcher;

public class MockItemMatcher extends ItemMatcher {

  @Override
  public double getMatchProbabilityInPercentage() {
    String nameA = getItemA().getName();
    String nameB = getItemA().getName();
    return normalizeScoreToPercentageGivenRange(nameA.compareTo(nameB), 0, 300);
  }

  @Override
  public boolean isMatching() {
    return getMatchProbabilityInPercentage() > 0.9;
  }
}
