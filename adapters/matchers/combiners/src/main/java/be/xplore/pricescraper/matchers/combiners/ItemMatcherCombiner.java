package be.xplore.pricescraper.matchers.combiners;

import be.xplore.pricescraper.matchers.ItemMatcher;
import be.xplore.pricescraper.utils.matchers.Matcher;
import java.util.HashMap;
import java.util.Map;

/**
 * Combines multiple matchers and aggregates results.
 */
public abstract class ItemMatcherCombiner implements Matcher {
  private final double matchThreshold;
  private final Map<ItemMatcher, Double> matchers = new HashMap<>();

  protected ItemMatcherCombiner(double matchThreshold) {
    this.matchThreshold = matchThreshold;
  }

  protected double getMatchThreshold() {
    return matchThreshold;
  }

  protected Map<ItemMatcher, Double> getMatchers() {
    return matchers;
  }

  /**
   * Adds a matcher to the combiner.
   */
  public void addMatcher(ItemMatcher matcher, double weight) {
    if (matchers.values().stream().mapToDouble(Double::doubleValue).sum() + weight > 1.0) {
      throw new IllegalArgumentException(
          "Matcher Combiner Weight should not go over 1 after calculating the sum");
    }
    matchers.put(matcher, weight);
  }
}
