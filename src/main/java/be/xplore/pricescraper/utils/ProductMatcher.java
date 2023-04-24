package be.xplore.pricescraper.utils;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class ProductMatcher {

  private final int matchThreshold;
  private static LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

  public ProductMatcher(int threshold) {
    this.matchThreshold = threshold;
  }


  /**
   * Match 2 products by passing their ingredient strings. Shops often desribe product names slightly different,
   * by checking the ingredient list we can verify that two products are the same as these should be described
   * accurately by the vendors.
   *
   * @return boolean is same product
   */
  public boolean isSameProductByIngredients(String ingredients1, String ingredients2) {
    return matchProductsByIngredients(ingredients1, ingredients2) < matchThreshold;
  }

  /**
   * Match 2 products by passing their ingredient strings. Shops often desribe product names slightly different,
   * by checking the ingredient list we can verify that two products are the same as these should be described
   * accurately by the vendors.
   *
   * @return degree matched (lower is better)
   */
  public int matchProductsByIngredients(String ingredients1, String ingredients2) {
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
   * Characters like "(", ")", ":", "," and whitespaces as well as plural forms of words will only obfscate the
   * fuzzy matching algorithm and should be removed
   *
   * @param ingredients
   * @return sanitized ingredients string
   */
  private String removeObfuscatingCharacters(String ingredients) {
    ingredients = removePlurals(ingredients);
    return ingredients.replaceAll("([,:()\\. ])|(\\r\\n|\\n|\\r)", "");
  }

  /**
   * remove (dutch) plurals
   * uienpoeder -> uipoeder
   * kinderen -> kind
   * varkens -> varken
   */
  private String removePlurals(String ingredients) {
    return ingredients.replaceAll("(s\\b)|(en)|(eren)", "");
  }

}
