package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.UnitType;
import be.xplore.pricescraper.exceptions.MatcherNotInitializedException;
import org.junit.jupiter.api.Test;


class ItemIngredientMatcherTests {

  //pizza salami
  private static final String delhaizePizzaIngredients =
      "TARWEBLOEM, 14% gezeefde tomaten, 12% MOZZARELLA KAAS, 12% salami (varkensvlees, spek, zout, specerijen, dextrose, uipoeder, knoflookpoeder, specerijenextracten, stabilisator (natriumnitriet), antioxidant (extract van rozemarijn), rook), water, 7,3% tomatenconcentraat, 4,6% EDAMMER KAAS, raapzaadolie, bakkersgist, zout, suiker, olijfolie extra vierge, oregano, basilicum, peper, paprika, peterselie. Kan bevatten (SOJA, MOSTERD).";
  private static final String ahPizzaIngredients =
      "Ingrediënten: TARWEBLOEM, 14% gezeefde tomaten, 12% MOZZARELLA KAAS, 12% salami (varkensvlees, spek, zout, specerijen, dextrose, uienpoeder, knoflookpoeder, specerijenextracten, stabilisator (natriumnitriet), antioxidant (extract van rozemarijn), rook), water, 7,3% tomatenconcentraat, 4,6% EDAMMER KAAS, raapzaadolie, bakkersgist, zout, suiker, olijfolie extra vierge, oregano, basilicum, peper, paprika, peterselie. Kan bevatten: SOJA, MOSTERD.";

  //pizza margherita
  private static final String ahOtherPizzaIngredients =
      "Ingrediënten: TARWEBLOEM, 15% gezeefde tomaten, 12% MOZZARELLA KAAS, 12% GOUDA KAAS, water, 7,7% tomatenconcentraat, raapzaadolie, bakkersgist, suiker, zout, oregano, basilicum, paprika, peterselie. Kan bevatten: SOJA, MOSTERD. Geschikt voor vegetariërs (gebruikte kaas bevat vegetarisch stremsel).";
  private static final Item delhaizePizza1 =
      new Item(1, "Pizza salami", "", 1, UnitType.kg, 1, delhaizePizzaIngredients, null);
  private static final Item ahPizza1 =
      new Item(1, "Pizza salami", "", 1, UnitType.kg, 1, ahPizzaIngredients, null);
  private static final Item ahPizza2 =
      new Item(1, "Pizza margherita", "", 1, UnitType.kg, 1, ahOtherPizzaIngredients, null);


  @Test
  void shouldMatchProducts() {
    IngredientMatcher itemIngredientMatcher = new IngredientMatcher();
    itemIngredientMatcher.addItems(delhaizePizza1, ahPizza1);
    boolean matched =
        itemIngredientMatcher.isMatching();
    assertThat(matched).isTrue();
  }

  @Test
  void shouldNotBeInitialized() {
    IngredientMatcher itemIngredientMatcher = new IngredientMatcher();
    assertThatThrownBy(itemIngredientMatcher::isMatching).isInstanceOf(
        MatcherNotInitializedException.class);
  }

  @Test
  void shouldNotMatchProducts() {
    IngredientMatcher itemIngredientMatcher = new IngredientMatcher();
    itemIngredientMatcher.addItems(delhaizePizza1, ahPizza2);
    boolean notMatched =
        itemIngredientMatcher.isMatching();
    assertThat(notMatched).isFalse();
  }

}
