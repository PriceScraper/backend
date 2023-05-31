package be.xplore.pricescraper.entity.shops;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The shop that a {@link TrackedItemEntity} is linked to.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Shop")
@Getter
@Setter
public class ShopEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String url; //root domain, example: carrefour.eu
}
