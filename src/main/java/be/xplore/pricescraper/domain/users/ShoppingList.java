package be.xplore.pricescraper.domain.users;

import be.xplore.pricescraper.domain.shops.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * The shopping list from a {@link User} which contains a list of {@link Item}.
 */
@NoArgsConstructor
@Entity
public class ShoppingList {
  @Id
  private int id;
  private String title;
  @ManyToMany
  private List<Item> items;
}
