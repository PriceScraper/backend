package be.xplore.pricescraper.utils.matchers;

import be.xplore.pricescraper.domain.shops.Item;

/**
 * Interface for matching Objects based on shared characteristics.
 */
public interface Matcher {

  void addItems(Item a, Item b);

  double getMatchProbabilityInPercentage();

  boolean isMatching();

}
