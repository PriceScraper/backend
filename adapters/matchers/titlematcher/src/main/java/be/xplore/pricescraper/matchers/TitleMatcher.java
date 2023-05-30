package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.matchers.utils.MatchStringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 * This is an {@link ItemMatcher} implementation.
 * The implmentation is based on {@link be.xplore.pricescraper.domain.shops.Item} titles/names.
 */
public class TitleMatcher extends ItemMatcher {

  private static final LevenshteinDistance levenshteinDistance =
      LevenshteinDistance.getDefaultInstance();
  private static final double matchThreshold = 0.7;

  @Override
  public boolean matchingIsPossible() {
    return getItemA().getName() != null && getItemB().getName() != null
        && !getItemA().getName().isEmpty() && !getItemB().getName().isEmpty();
  }

  /**
   * Match 2 items by passing their title strings.
   * Shops often desribe product names slightly different.
   *
   * @return boolean is same item
   */
  @Override
  public boolean isMatching() {
    validateIsInitialized();
    double matchProbability = getMatchProbabilityInPercentage();
    return matchProbability >= matchThreshold;
  }

  @Override
  public double getMatchProbabilityInPercentage() {
    int score = matchitemsByTitles(getItemA().getName(), getItemB().getName());
    int maxLength = MatchStringUtils.getMaxSizeOfStrings(getItemA().getName(),
        getItemB().getName());
    return normalizeScoreToPercentageGivenRange(score, 0, maxLength);
  }

  /**
   * Match 2 items by passing their title strings.
   * Shops often desribe product names slightly different,
   * by checking the ingredient list we can verify that two items
   * are the same as these should be described.
   * accurately by the vendors.
   *
   * @return degree matched (lower is better)
   */
  private int matchitemsByTitles(String title1, String title2) {
    title1 = normalizeTitleString(title1);
    title2 = normalizeTitleString(title2);
    return levenshteinDistance.apply(title1, title2);
  }

  private String normalizeTitleString(String title) {
    return MatchStringUtils.normalizeString(title);
  }
}
