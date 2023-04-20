package be.xplore.pricescraper.domain.shops;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.sql.Timestamp;
import lombok.NoArgsConstructor;

/**
 * The price linked to an {@link Item} at a specific timestamp.
 */
@NoArgsConstructor
@Entity
public class ItemPrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private Timestamp timestamp;
  private double price;
  @ManyToOne
  private TrackedItem trackedItem;

}
