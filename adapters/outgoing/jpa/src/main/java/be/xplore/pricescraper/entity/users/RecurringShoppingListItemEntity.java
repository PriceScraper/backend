package be.xplore.pricescraper.entity.users;

import be.xplore.pricescraper.entity.shops.ItemEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity of RecurringShoppingListItem.
 */
@Entity(name = "RecurringShoppingListItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringShoppingListItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private long id;
  @ManyToOne
  @JoinColumn(name = "item_id")
  private ItemEntity item;
  private int quantity;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
}
