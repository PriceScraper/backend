package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.exceptions.MatcherNotInitializedException;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

/**
 * This is an {@link ItemMatcher} implementation.
 * The implmentation is based on {@link be.xplore.pricescraper.domain.shops.Item} ingredients.
 */
@Component
public class IngredientMatcher extends ItemMatcher {

  private static final LevenshteinDistance levenshteinDistance =
      LevenshteinDistance.getDefaultInstance();
  private static final double matchThreshold = 0.9;

  /**
   * Match 2 items by passing their ingredient strings.
   * Shops often desribe product names slightly different,
   * by checking the ingredient list we can verify that two items
   * are the same as these should be described.
   * accurately by the vendors.
   *
   * @return boolean is same product
   */
  @Override
  public boolean isMatching() {
    if (!isInitialized()) {
      throw new MatcherNotInitializedException();
    }
    double matchProbability = getMatchProbabilityInPercentage();
    return matchProbability >= matchThreshold;
  }

  @Override
  public double getMatchProbabilityInPercentage() {
    int score = matchitemsByIngredients(getItemA().getIngredients(), getItemB().getIngredients());
    return normalizeScoreToPercentageGivenRange(score, 0, getMaxIngredientsStringSize());
  }

  private int getMaxIngredientsStringSize() {
    return Math.max(getItemA().getIngredients().length(), getItemB().getIngredients().length());
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
    ingredients = ingredients.toLowerCase();
    ingredients = removeLiteralIngredientsString(ingredients);
    ingredients = removeObfuscatingCharacters(ingredients);
    return ingredients;
  }

  private String removeLiteralIngredientsString(String ingredients) {
    return ingredients.replace("ingrediënten", "");
  }

  /**
   * Removes obfuscating characters from a string.
   * Characters like "(", ")", ":", "," and whitespaces as well as plural forms of words will
   * only obfscate the fuzzy matching algorithm and should be removed
   *
   * @param ingredients ingredient contents as found on the product source (webpage)
   * @return sanitized ingredients string
   */
  private String removeObfuscatingCharacters(String ingredients) {
    ingredients = removePlurals(ingredients);
    return ingredients.replaceAll("([,:()\\. ])|(\\r\\n|\\n|\\r)", "");
  }

  /**
   * Removes plurals of words.
   * remove (dutch) plurals
   * uienpoeder -> uipoeder
   * kinderen -> kind
   * varkens -> varken
   * At the moment this couples us tightly to the dutch language and could be abstracted in the
   * future if needed.
   */
  private String removePlurals(String ingredients) {
    return ingredients.replaceAll("(s\\b)|(en)|(eren)", "");
  }

}
