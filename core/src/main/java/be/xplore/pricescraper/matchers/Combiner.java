package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.domain.shops.Item;

/**
 * Interface for a matcher combiner.
 */
public interface Combiner {
  void addItems(Item a, Item b);

  void addMatcher(Matcher m);

  boolean isMatching();
}
