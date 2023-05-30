package be.xplore.pricescraper.matchers.combiners;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.matchers.Combiner;
import be.xplore.pricescraper.matchers.Matcher;
import java.util.ArrayList;
import java.util.List;

/**
 * Combines multiple matchers and aggregates results.
 */
public abstract class ItemMatcherCombiner implements Combiner {
  private final List<Matcher> matchers = new ArrayList<>();

  protected List<Matcher> getMatchers() {
    return matchers;
  }

  @Override
  public void addMatcher(Matcher m) {
    matchers.add(m);
  }

  @Override
  public void addItems(Item a, Item b) {
    for (Matcher m :
        matchers) {
      m.addItems(a, b);
    }
  }

}
