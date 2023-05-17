package be.xplore.pricescraper.factories;


import be.xplore.pricescraper.utils.matchers.Combiner;

/**
 * Factory for creating Item Matcher Combiners.
 */
public interface ItemMatcherCombinerFactory {
  Combiner makeWeightedCombiner();
}
