package be.xplore.pricescraper.utils.matchers;

/**
 * Interface for matching Objects based on shared characteristics.
 */
public interface Matcher {

  double getMatchProbabilityInPercentage();

  boolean isMatching();

}
