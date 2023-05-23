package be.xplore.pricescraper.domain.recipes;

import be.xplore.pricescraper.domain.shops.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Linked item with Recipe, along with quantity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeItem {
  private long id;
  private Recipe recipe;
  private Item item;
  private int quantity;

  /**
   * Constructor.
   */
  public RecipeItem(Recipe recipe, Item item, int quantity) {
    this.recipe = recipe;
    this.item = item;
    this.quantity = quantity;
  }
}
