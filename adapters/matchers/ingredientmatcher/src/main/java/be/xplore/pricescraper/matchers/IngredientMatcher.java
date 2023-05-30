package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.matchers.utils.MatchStringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 * This is an {@link ItemMatcher} implementation.
 * The implmentation is based on {@link be.xplore.pricescraper.domain.shops.Item} ingredients.
 */
public class IngredientMatcher extends ItemMatcher {

  private static final LevenshteinDistance levenshteinDistance =
      LevenshteinDistance.getDefaultInstance();
  private static final double matchThreshold = 0.7;

  @Override
  public boolean matchingIsPossible() {
    return getItemA().getIngredients() != null && getItemB().getIngredients() != null
        && !getItemA().getIngredients().isEmpty() && !getItemB().getIngredients().isEmpty();
  }

  /**
   * Match 2 items by passing their ingredient strings.
   * Shops often desribe product names slightly different,
   * by checking the ingredient list we can verify that two items
   * are the same as these should be described.
   * accurately by the vendors.
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
    int score = matchitemsByIngredients(getItemA().getIngredients(), getItemB().getIngredients());
    int maxLength = MatchStringUtils.getMaxSizeOfStrings(getItemA().getIngredients(),
        getItemB().getIngredients());
    return normalizeScoreToPercentageGivenRange(score, 0, maxLength);
  }

  /**
   * Match 2 items by passing their ingredient strings.
   * Shops often desribe product names slightly different,
   * by checking the ingredient list we can verify that two items
   * are the same as these should be described.
   * accurately by the vendors.
   *
   * @return degree matched (lower is better)
   */
  private int matchitemsByIngredients(String ingredients1, String ingredients2) {
    ingredients1 = normalizeIngredientsString(ingredients1);
    ingredients2 = normalizeIngredientsString(ingredients2);
    return levenshteinDistance.apply(ingredients1, ingredients2);
  }

  private String normalizeIngredientsString(String ingredients) {
    ingredients = removeLiteralIngredientsString(ingredients);
    ingredients = MatchStringUtils.normalizeString(ingredients);
    return ingredients;
  }

  private String removeLiteralIngredientsString(String ingredients) {
    return ingredients.replace("ingrediënten", "");
  }
}
