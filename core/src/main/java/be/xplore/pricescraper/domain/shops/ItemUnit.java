package be.xplore.pricescraper.domain.shops;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * The unit of an item (1kg,1l) in which it is sold.
 */
@NoArgsConstructor
@AllArgsConstructor
public class ItemUnit {
  
  private UnitType type;
  private double content;

  /**
   * Type of the unit (kg,l).
   */
  public enum UnitType {
    KILOGRAMS,
    LITERS
  }

}
