package be.xplore.pricescraper.domain.shops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
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
public class ItemPrice implements Serializable {
  private int id;
  private Instant timestamp;
  private double price;
  @JsonIgnore
  private TrackedItem trackedItem;
}
