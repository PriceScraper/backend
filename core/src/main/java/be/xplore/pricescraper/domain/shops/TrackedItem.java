package be.xplore.pricescraper.domain.shops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
@Getter
@Setter
public class TrackedItem {
  private String url;
  private Shop shop;
  @JsonIgnore
  private Item item;
  private List<ItemPrice> itemPrices;
  private Timestamp lastAttempt;

  /**
   * Constructor with last attempt as now, minus 7 days to initialize next scrape.
   */
  public TrackedItem(String url, Shop shop, Item item) {
    this.url = url;
    this.shop = shop;
    this.item = item;
    this.lastAttempt = Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS));
  }
}
