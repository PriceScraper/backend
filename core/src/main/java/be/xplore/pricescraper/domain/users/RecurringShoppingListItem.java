package be.xplore.pricescraper.domain.users;

import be.xplore.pricescraper.domain.shops.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity that represents an item that has to be added in every new shopping list.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringShoppingListItem {
  private long id;
  private Item item;
  private int quantity;
  private User user;

  /**
   * Constructor.
   */
  public RecurringShoppingListItem(Item item, int quantity, User user) {
    this.item = item;
    this.quantity = quantity;
    this.user = user;
  }
}
