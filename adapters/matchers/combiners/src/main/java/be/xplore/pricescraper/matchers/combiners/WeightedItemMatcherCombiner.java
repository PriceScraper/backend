package be.xplore.pricescraper.matchers.combiners;

import be.xplore.pricescraper.exceptions.CombinerInitializeException;
import be.xplore.pricescraper.exceptions.MatchException;
import be.xplore.pricescraper.matchers.Matcher;
import java.util.HashMap;
import java.util.Map;

/**
 * Weighted implementation for {@link ItemMatcherCombiner}.
 */
public class WeightedItemMatcherCombiner extends ItemMatcherCombiner {

  private final Map<Matcher, Double> weightedMatchers = new HashMap<>();
  private static final double MATCH_THRESHOLD = 0.8;

  @Override
  public boolean isMatching() {
    double matchPercentage = getMatchProbabilityInPercentage();
    return matchPercentage >= getMatchThreshold();
  }

  private double getMatchThreshold() {
    return MATCH_THRESHOLD;
  }

  /**
   * Gets combines match probability for matchers.
   */
  public double getMatchProbabilityInPercentage() {
    double percentageSum = 0;
    double percentageTotal = 1.0;
    for (Map.Entry<Matcher, Double> m :
        weightedMatchers.entrySet()) {
      if (!m.getKey().matchingIsPossible()) {
        percentageTotal -= m.getValue();
        continue;
      }
      percentageSum += m.getKey().getMatchProbabilityInPercentage() * m.getValue();
    }
    if (percentageTotal == 0.0) {
      throw new MatchException("Unable to match, none of the matchers were applicable");
    }
    return percentageSum / percentageTotal;
  }

  /**
   * Adds a weighted matcher.
   */
  public void addWeightToMatcher(Class<? extends Matcher> matcherClass, double weight) {
    weightedMatchers.put(getMatcherByType(matcherClass), weight);
  }

  private Matcher getMatcherByType(Class<? extends Matcher> matcherClass) {
    return getMatchers().stream()
        .filter(m -> m.getClass().isAssignableFrom(matcherClass)).findFirst()
        .orElseThrow(CombinerInitializeException::new);
  }

}
