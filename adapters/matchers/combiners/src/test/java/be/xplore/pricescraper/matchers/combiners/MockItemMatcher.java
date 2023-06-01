package be.xplore.pricescraper.matchers.combiners;

import be.xplore.pricescraper.matchers.ItemMatcher;

public class MockItemMatcher extends ItemMatcher {

  @Override
  public boolean matchingIsPossible() {
    return getItemA().getName() != null && getItemB().getName() != null
        && !getItemA().getName().isEmpty() && !getItemB().getName().isEmpty();
  }

  @Override
  public double getMatchProbabilityInPercentage() {
    String nameA = getItemA().getName();
    String nameB = getItemA().getName();
    return normalizeScoreToPercentageGivenRange(nameA.compareTo(nameB), 0, 300);
  }
}
