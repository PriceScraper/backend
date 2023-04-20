package be.xplore.pricescraper.domain.shopping;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;

/**
 * The price linked to an {@link Item} at a specific timestamp.
 */
@RequiredArgsConstructor
@Entity
public class ItemPrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private Timestamp timestamp;
  private double price;
  @OneToOne
  private Item item;

}
