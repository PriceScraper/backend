package be.xplore.pricescraper.domain.shops;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The shop that a {@link TrackedItem} is linked to.
 */
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Shop {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String url; //root domain, example: carrefour.eu
}
