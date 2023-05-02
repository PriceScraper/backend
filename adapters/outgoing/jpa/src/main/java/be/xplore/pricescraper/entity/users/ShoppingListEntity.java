package be.xplore.pricescraper.entity.users;

import be.xplore.pricescraper.entity.shops.ItemEntity;
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
 * The shopping list from a {@link UserEntity} which contains a list of {@link ItemEntity}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "ShoppingList")
public class ShoppingListEntity {
  @Setter
  @Id
  private int id;
  @Setter
  private String title;
  @Setter
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShoppingListLineEntity> lines;
}
