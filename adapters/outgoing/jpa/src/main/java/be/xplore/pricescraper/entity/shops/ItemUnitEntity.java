package be.xplore.pricescraper.entity.shops;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * The unit of an item (1kg,1l) in which it is sold.
 */
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ItemUnitEntity {
  @Column
  @Enumerated
  private UnitType type;
  @Column
  private double content;

  /**
   * Type of the unit (kg,l).
   */
  public enum UnitType {
    KILOGRAMS,
    LITERS
  }

}
