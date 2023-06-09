package be.xplore.pricescraper.entity.shops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The price linked to an {@link TrackedItemEntity} at a specific timestamp.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ItemPrice")
@Getter
@Setter
public class ItemPriceEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;
  private Instant timestamp;
  private double price;
  @JsonIgnore
  @ManyToOne
  private TrackedItemEntity trackedItem;
}
