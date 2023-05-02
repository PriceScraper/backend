package be.xplore.pricescraper.domain.shops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The price linked to an {@link TrackedItem} at a specific timestamp.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemPrice {
  private int id;
  private Timestamp timestamp;
  private double price;
  @JsonIgnore
  private TrackedItem trackedItem;
}
