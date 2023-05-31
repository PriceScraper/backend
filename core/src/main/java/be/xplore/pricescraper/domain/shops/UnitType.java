package be.xplore.pricescraper.domain.shops;

/**
 * Unit.
 */
public enum UnitType {
  KG(1000, getMass()),
  G(1, getMass()),
  L(1000, getVolume()),
  ML(1, getVolume()),
  CL(10, getVolume()),
  NOT_AVAILABLE(0, "");

  public final int unitWeightValue;
  public final String category;

  UnitType(int unitWeightValue, String category) {
    this.unitWeightValue = unitWeightValue;
    this.category = category;
  }

  private static String getVolume() {
    return "volume";
  }

  private static String getMass() {
    return "mass";
  }
}
