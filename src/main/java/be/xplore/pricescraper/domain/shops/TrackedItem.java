package be.xplore.pricescraper.domain.shops;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * A specific item from a {@link Shop} that is linked to a general {@link Item}.
 */
@NoArgsConstructor
@Entity
public class TrackedItem {
  @Id
  private String url;
  @ManyToOne
  private Shop shop;
  @ManyToOne
  private Item item;
  @OneToMany
  private List<ItemPrice> itemPrices;
}
