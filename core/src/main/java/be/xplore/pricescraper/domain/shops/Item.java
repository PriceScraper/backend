package be.xplore.pricescraper.domain.shops;

import java.io.Serializable;
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
@Getter
@Setter
public class Item implements Serializable {
  private int id;
  private String name;
  private String image;
  private int quantity;
  private UnitType type;
  private double amount;
  private String ingredients;
  private List<TrackedItem> trackedItems;

  /**
   * Constructor without id and trackedItems.
   */
  public Item(String name, String image, int quantity, UnitType type, double amount,
              String ingredients) {
    this.name = name;
    this.image = image;
    this.quantity = quantity;
    this.type = type;
    this.amount = amount;
    this.ingredients = ingredients;
  }
}
