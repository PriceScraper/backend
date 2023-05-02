package be.xplore.pricescraper.domain.users;

import be.xplore.pricescraper.domain.shops.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Entity
@Setter
public class ShoppingListLine {
  @Id
  private int id;
  private int quantity;
  @ManyToOne
  private Item item;
}
