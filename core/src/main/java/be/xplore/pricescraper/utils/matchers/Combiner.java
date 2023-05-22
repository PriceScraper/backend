package be.xplore.pricescraper.utils.matchers;

import be.xplore.pricescraper.domain.shops.Item;

/**
 * Interface for a matcher combiner.
 */
public interface Combiner {
  void addItems(Item a, Item b);

  boolean isMatching();
}
