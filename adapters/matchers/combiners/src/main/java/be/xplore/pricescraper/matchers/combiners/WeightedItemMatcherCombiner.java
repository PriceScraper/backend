package be.xplore.pricescraper.matchers.combiners;

import be.xplore.pricescraper.exceptions.MatchException;
import be.xplore.pricescraper.matchers.ItemMatcher;
import java.util.Map;

/**
 * Weighted implementation for {@link ItemMatcherCombiner}.
 */
public class WeightedItemMatcherCombiner extends ItemMatcherCombiner {

  private static final double matchThreshold = 0.75;

  protected double getMatchThreshold() {
    return matchThreshold;
  }

  /**
   * Gets combines match probability for matchers.
   */
  public double getMatchProbabilityInPercentage() {
    if (getWeightSum() != 1.0) {
      throw new MatchException("Sum of weights in match combiner should be equal to 1");
    }
    double percentageSum = 0;
    for (Map.Entry<ItemMatcher, Double> m :
        getMatchers().entrySet()) {
      percentageSum += m.getKey().getMatchProbabilityInPercentage() * m.getValue();
    }
    return percentageSum;
  }

  private double getWeightSum() {
    return getMatchers().values().stream().mapToDouble(Double::doubleValue).sum();
  }

  @Override
  public boolean isMatching() {
    return getMatchProbabilityInPercentage() >= getMatchThreshold();
  }
}
