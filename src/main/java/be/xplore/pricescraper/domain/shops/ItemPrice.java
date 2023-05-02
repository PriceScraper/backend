package be.xplore.pricescraper.domain.shops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Entity
@Getter
@Setter
public class ItemPrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;
  private Timestamp timestamp;
  private double price;
  @JsonIgnore
  @ManyToOne
  private TrackedItem trackedItem;
}
