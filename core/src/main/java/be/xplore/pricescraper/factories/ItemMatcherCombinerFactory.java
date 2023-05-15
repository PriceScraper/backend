package be.xplore.pricescraper.factories;


import be.xplore.pricescraper.utils.matchers.Matcher;
import java.util.List;

/**
 * Factory for creating Item Matcher Combiners.
 */
public interface ItemMatcherCombinerFactory {
  Matcher makeWeightedCombiner(String name, List<Matcher> matchers, List<Integer> weights);
}
