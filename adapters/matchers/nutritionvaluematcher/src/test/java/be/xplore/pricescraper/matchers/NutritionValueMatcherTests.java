package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.UnitType;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class NutritionValueMatcherTests {

  private static final Item itemA =
      new Item("Halfvolle melk AH", "", 1, UnitType.ML, 300, "", null);
  private static final Item itemB =
      new Item("Halfvolle melk", "", 1, UnitType.ML, 300, "", null);

  static {
    itemA.setNutritionValues(new HashMap<>());
    itemA.getNutritionValues().put("energie", "87kcal");
    itemA.getNutritionValues().put("vetten", "1g");
    itemA.getNutritionValues().put("proteïnen", "7g");
    itemA.getNutritionValues().put("koolhydraten", "18g");
    itemB.setNutritionValues(new HashMap<>());
    itemB.getNutritionValues().put("Energie", "87 kcal");
    itemB.getNutritionValues().put("vetten", "1 g");
    itemB.getNutritionValues().put("Proteïnen", "7g");
    itemB.getNutritionValues().put("Koolhydraten", "24g");
  }

  @Test
  void shouldMatch() {
    Matcher matcher = new NutritionValueMatcher();
    matcher.addItems(itemA, itemB);
    assertThat(matcher.getMatchProbabilityInPercentage()).isGreaterThan(0.74);
  }

  @Test
  void shouldNotMatch() {
    Matcher matcher = new NutritionValueMatcher();
    matcher.addItems(itemA, itemB);
    assertThat(matcher.getMatchProbabilityInPercentage()).isLessThan(0.76);
  }

}
