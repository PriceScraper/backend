package be.xplore.pricescraper.matchers.utils;

/**
 * Utilities for strings in a matchking context.
 */
public class MatchStringUtils {

  private MatchStringUtils() {
  }

  /**
   * Normalize characters for better match results (trivial characters get left out).
   */
  public static String normalizeString(String matchStr) {
    matchStr = matchStr.toLowerCase();
    matchStr = removeObfuscatingCharacters(matchStr);
    return matchStr;
  }

  /**
   * Removes obfuscating characters from a string.
   * Characters like "(", ")", ":", "," and whitespaces as well as plural forms of words.
   */
  private static String removeObfuscatingCharacters(String matchStr) {
    matchStr = removePlurals(matchStr);
    return matchStr.replaceAll("([,:()\\. ])|(\\r\\n|\\n|\\r)", "");
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
  private static String removePlurals(String ingredients) {
    return ingredients.replaceAll("(s\\b)|(en)|(eren)", "");
  }

  /**
   * Gets max size of two strings.
   */
  public static int getMaxSizeOfStrings(String a, String b) {
    return Math.max(a.length(), b.length());
  }

}
