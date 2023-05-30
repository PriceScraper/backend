package be.xplore.pricescraper.domain.shops;

/**
 * Unit.
 */
public enum UnitType {
  kg(1000, "mass"),
  g(1, "mass"),
  l(1000, "volume"),
  ml(1, "volume"),
  cl(10, "volume"),
  not_available(0, "");

  public final int unitWeightValue;
  public final String category;

  private UnitType(int unitWeightValue, String category) {
    this.unitWeightValue = unitWeightValue;
    this.category = category;
  }
}
