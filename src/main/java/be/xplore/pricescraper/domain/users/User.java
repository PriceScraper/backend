package be.xplore.pricescraper.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * The User of our application.
 * {@code shoppingLists} are the shopping lists of this User
 */
@NoArgsConstructor
@Entity
public class User {
  @Id
  private String email;
  @OneToMany
  private List<ShoppingList> shoppingLists;

}
