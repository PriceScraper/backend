package be.xplore.pricescraper.entity.shops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A specific item from a {@link ShopEntity} that is linked to a general {@link ItemEntity}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TrackedItem")
@Getter
@Setter
public class TrackedItemEntity implements Serializable {
  @Id
  private String url;
  @ManyToOne
  private ShopEntity shop;
  @JsonIgnore
  @ManyToOne
  private ItemEntity item;
  @OneToMany(fetch = FetchType.LAZY)
  private List<ItemPriceEntity> itemPrices;
  private Timestamp lastAttempt;

  /**
   * Constructor with last attempt as now, minus 7 days to initialize next scrape.
   */
  public TrackedItemEntity(String url, ShopEntity shop, ItemEntity item) {
    this.url = url;
    this.shop = shop;
    this.item = item;
    this.lastAttempt = Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS));
  }
}
