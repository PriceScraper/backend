package be.xplore.pricescraper.domain.shops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The shop that a {@link TrackedItem} is linked to.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Shop {
  private int id;
  private String name;
  private String url; //root domain, example: carrefour.eu
}
