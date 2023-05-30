package be.xplore.pricescraper.factories;

import be.xplore.pricescraper.matchers.Combiner;
import be.xplore.pricescraper.matchers.Matcher;
import java.util.Map;

/**
 * Factory for creating Item Matcher Combiners.
 */
public interface ItemMatcherCombinerFactory {
  Combiner makeWeightedCombiner(Map<Matcher, Double> matchersWithWeights);
}
