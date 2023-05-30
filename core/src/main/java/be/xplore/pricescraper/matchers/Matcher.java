package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.domain.shops.Item;

/**
 * Interface for matching Objects based on shared characteristics.
 */
public interface Matcher {
  boolean matchingIsPossible();

  void addItems(Item a, Item b);

  double getMatchProbabilityInPercentage();

  boolean isMatching();

}
