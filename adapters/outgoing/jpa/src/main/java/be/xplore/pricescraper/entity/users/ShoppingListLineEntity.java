package be.xplore.pricescraper.entity.users;

import be.xplore.pricescraper.entity.shops.ItemEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Setter
@Entity(name = "ShoppingListLine")
public class ShoppingListLineEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int quantity;
  @ManyToOne
  private ItemEntity item;
}
