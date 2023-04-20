package be.xplore.pricescraper.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProductMatcherTests {

  //pizza salami
  private static final String delhaizePizzaIngredients =
      "TARWEBLOEM, 14% gezeefde tomaten, 12% MOZZARELLA KAAS, 12% salami (varkensvlees, spek, zout, specerijen, dextrose, uipoeder, knoflookpoeder, specerijenextracten, stabilisator (natriumnitriet), antioxidant (extract van rozemarijn), rook), water, 7,3% tomatenconcentraat, 4,6% EDAMMER KAAS, raapzaadolie, bakkersgist, zout, suiker, olijfolie extra vierge, oregano, basilicum, peper, paprika, peterselie. Kan bevatten (SOJA, MOSTERD).";
  private static final String ahPizzaIngredients =
      "Ingrediënten: TARWEBLOEM, 14% gezeefde tomaten, 12% MOZZARELLA KAAS, 12% salami (varkensvlees, spek, zout, specerijen, dextrose, uienpoeder, knoflookpoeder, specerijenextracten, stabilisator (natriumnitriet), antioxidant (extract van rozemarijn), rook), water, 7,3% tomatenconcentraat, 4,6% EDAMMER KAAS, raapzaadolie, bakkersgist, zout, suiker, olijfolie extra vierge, oregano, basilicum, peper, paprika, peterselie. Kan bevatten: SOJA, MOSTERD.";
  private static final String carrefourPizzaIngredients =
      "Tarwebloem\n" + "14 % gezeefde tomaten\n" + "12 % mozzarella kaas\n" +
          "12 % salami (varkensvlees, spek, zout specerijen, dextrose, uienpoeder, knoflookpoeder, specerijenextracten, stabilisator (natriumnitriet), antioxidant (extract van rozemarijn), rook)\n" +
          "water\n" + "7.3 % tomatenconcentraat\n" + "4.6 % edammer kaas\n" + "raapzaadolie\n" +
          "bakkersgist\n" + "zout\n" + "suiker\n" + "olijfolie extra vierge\n" + "oregano\n" +
          "basilicum\n" + "peper\n" + "paprika\n" + "peterselie";
  //pizza margherita
  private static final String ahOtherPizzaIngredients =
      "Ingrediënten: TARWEBLOEM, 15% gezeefde tomaten, 12% MOZZARELLA KAAS, 12% GOUDA KAAS, water, 7,7% tomatenconcentraat, raapzaadolie, bakkersgist, suiker, zout, oregano, basilicum, paprika, peterselie. Kan bevatten: SOJA, MOSTERD. Geschikt voor vegetariërs (gebruikte kaas bevat vegetarisch stremsel).";
  ProductMatcher productMatcher = new ProductMatcher(50);

  @Test
  void matchByIngredientsReturnsPercentage() {
    int degreeMatched = productMatcher.matchProductsByIngredients(delhaizePizzaIngredients,
        ahOtherPizzaIngredients);
    assertTrue(degreeMatched >= 0);
  }

  @Test
  void shouldMatchProducts() {
    boolean matched =
        productMatcher.isSameProductByIngredients(delhaizePizzaIngredients, ahPizzaIngredients);
    assertTrue(matched);
  }

  @Test
  void shouldNotMatchProducts() {
    boolean notMatched =
        productMatcher.isSameProductByIngredients(ahPizzaIngredients, ahOtherPizzaIngredients);
    assertFalse(notMatched);
  }


}
