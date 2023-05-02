package be.xplore.pricescraper.domain.users;

import be.xplore.pricescraper.domain.shops.Item;
import java.util.List;
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
public class ShoppingList {
  @Setter
  private int id;
  @Setter
  private String title;
  @Setter
  private List<ShoppingListLine> lines;
}
