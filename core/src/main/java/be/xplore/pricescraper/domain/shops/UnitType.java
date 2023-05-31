package be.xplore.pricescraper.domain.shops;

/**
 * Unit.
 */
public enum UnitType {
  KG(1000, "mass"),
  G(1, "mass"),
  L(1000, "volume"),
  ML(1, "volume"),
  CL(10, "volume"),
  NOT_AVAILABLE(0, "");

  public final int unitWeightValue;
  public final String category;

  UnitType(int unitWeightValue, String category) {
    this.unitWeightValue = unitWeightValue;
    this.category = category;
  }
}
