package be.xplore.pricescraper.domain.shops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A specific item from a {@link Shop} that is linked to a general {@link Item}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class TrackedItem {
  @Id
  private String url;
  @ManyToOne
  private Shop shop;
  @JsonIgnore
  @ManyToOne
  private Item item;
  @Setter
  @OneToMany(fetch = FetchType.LAZY)
  private List<ItemPrice> itemPrices;

}
