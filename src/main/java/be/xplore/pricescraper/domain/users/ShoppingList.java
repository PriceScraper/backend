package be.xplore.pricescraper.domain.users;

import be.xplore.pricescraper.domain.shops.Item;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Entity
public class ShoppingList {
  @Id
  private int id;
  @Setter
  private String title;
  @Setter
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShoppingListLine> lines;
}
