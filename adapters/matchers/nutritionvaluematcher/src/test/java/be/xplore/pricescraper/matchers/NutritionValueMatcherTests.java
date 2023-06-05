package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.UnitType;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class NutritionValueMatcherTests {

  private static final Item itemA =
      new Item("Halfvolle melk", "", 1, UnitType.ML, 300, "",
          new HashMap<>());
  private static final Item itemB =
      new Item("Halfvolle melk", "", 1, UnitType.ML, 300, "",
          new HashMap<>());
  private static final Item itemC =
      new Item("Halfvolle melk", "", 1, UnitType.ML, 300, "",
          new HashMap<>());
  private static final Item itemD =
      new Item("Halfvolle melk", "", 1, UnitType.ML, 300, "",
          new HashMap<>());

  static {
    itemA.getNutritionValues().put("energie", "87kcal");
    itemA.getNutritionValues().put("vetten", "1g");
    itemA.getNutritionValues().put("proteïnen", "7g");
    itemA.getNutritionValues().put("koolhydraten", "18g");

    itemB.getNutritionValues().put("Energie", "87 kcal");
    itemB.getNutritionValues().put("vetten", "1 g");
    itemB.getNutritionValues().put("Proteïnen", "7g");
    itemB.getNutritionValues().put("Koolhydraten", "24g");

    itemC.getNutritionValues().put("Energie", "76 kJ/18 kcal");
    itemD.getNutritionValues().put("energie", " 76 kJ (18 kcal)");
  }

  @Test
  void shouldMatch() {
    Matcher matcher = new NutritionValueMatcher();
    matcher.addItems(itemA, itemB);
    assertThat(matcher.getMatchProbabilityInPercentage()).isEqualTo(0.75);
  }

  @Test
  void energyShouldMatch() {
    Matcher matcher = new NutritionValueMatcher();
    matcher.addItems(itemC, itemD);
    assertThat(matcher.getMatchProbabilityInPercentage()).isEqualTo(1.0);
  }

}
