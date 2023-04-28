package be.xplore.pricescraper.domain.shops;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Definition of an Item, not related to a {@link Shop}.
 * The {@code specificItems} list contains {@link TrackedItem},
 * that are linked to a specific {@link Shop}
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;
  private String name;
  private String image;
  private int quantity;
  private ItemUnit unit;
  private String ingredients;
  @OneToMany(fetch = FetchType.LAZY)
  private List<TrackedItem> trackedItems;

  /**
   * Constructor without id and trackedItems.
   */
  public Item(String name, String image, int quantity, ItemUnit unit, String ingredients) {
    this.name = name;
    this.image = image;
    this.quantity = quantity;
    this.unit = unit;
    this.ingredients = ingredients;
  }
}
