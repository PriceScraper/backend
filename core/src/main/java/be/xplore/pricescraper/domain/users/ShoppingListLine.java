package be.xplore.pricescraper.domain.users;

import be.xplore.pricescraper.domain.shops.Item;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The shopping list from a {@link User} which contains a list of {@link Item}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShoppingListLine implements Serializable {
  private int id;
  private int quantity;
  private Item item;

  public ShoppingListLine(int quantity, Item item) {
    this.quantity = quantity;
    this.item = item;
  }
}
