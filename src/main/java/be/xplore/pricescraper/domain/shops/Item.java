package be.xplore.pricescraper.domain.shops;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Definition of an Item, not related to a {@link Shop}.
 * The {@code specificItems} list contains {@link TrackedItem},
 * that are linked to a specific {@link Shop}
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Item {
  @Id
  private int id;
  private String name;
  private String image;
  private int quantity;
  private ItemUnit unit;
  private String ingredients;
  @OneToMany(fetch = FetchType.LAZY)
  private List<TrackedItem> trackedItems;
}
