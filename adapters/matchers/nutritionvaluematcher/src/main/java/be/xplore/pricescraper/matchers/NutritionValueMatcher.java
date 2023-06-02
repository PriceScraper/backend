package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.matchers.utils.MatchStringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an {@link ItemMatcher} implementation.
 * The implmentation is based on {@link be.xplore.pricescraper.domain.shops.Item} nutrition values.
 */
public class NutritionValueMatcher extends ItemMatcher {
  @Override
  public boolean matchingIsPossible() {
    return getItemA().getNutritionValues() != null && getItemB().getNutritionValues() != null
        && !getItemA().getNutritionValues().entrySet().isEmpty()
        && !getItemB().getNutritionValues().entrySet().isEmpty();
  }

  @Override
  public double getMatchProbabilityInPercentage() {
    validateIsInitialized();
    List<List<String>> matchingKeys = getMatchingKeys();
    int equalValuesCount = getEqualValuesCount(matchingKeys);
    return (double) equalValuesCount / matchingKeys.size();
  }

  private List<List<String>> getMatchingKeys() {
    List<List<String>> matchingKeys = new ArrayList<>();
    getItemA().getNutritionValues().keySet().forEach(
        keyA ->
            getItemB().getNutritionValues().keySet().stream().filter(
                    k -> MatchStringUtils.normalizeString(k)
                        .equals(MatchStringUtils.normalizeString(keyA))).findFirst()
                .ifPresent(keyB -> matchingKeys.add(List.of(keyA, keyB)))
    );
    return matchingKeys;
  }

  private int getEqualValuesCount(List<List<String>> matchingKeys) {
    int equalValues = 0;
    for (List<String> keys : matchingKeys) {
      if (normalizeEnergyValue(
          MatchStringUtils.normalizeString(getItemA().getNutritionValues().get(keys.get(0))))
          .equals(
              normalizeEnergyValue(MatchStringUtils.normalizeString(
                  getItemB().getNutritionValues().get(keys.get(1)))))) {
        equalValues++;
      }
    }
    return equalValues;
  }

  private String normalizeEnergyValue(String energyValue) {
    return energyValue.replaceAll("([()/]|[0-9]k[jJ])", "");
  }

}
