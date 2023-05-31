package be.xplore.pricescraper.matchers;

/**
 * This is an {@link ItemMatcher} implementation.
 * The implmentation is based on {@link be.xplore.pricescraper.domain.shops.Item} unit.
 */
public class UnitMatcher extends ItemMatcher {
  private static final double matchThreshold = 1;

  @Override
  public boolean isMatching() {
    return getMatchProbabilityInPercentage() >= matchThreshold;
  }

  @Override
  public boolean matchingIsPossible() {
    return getItemA().getType() != null && getItemA().getAmount() != 0.0
        && getItemB().getType() != null && getItemB().getAmount() != 0.0;
  }

  @Override
  public double getMatchProbabilityInPercentage() {
    double unitValueA = getItemA().getAmount() * getItemA().getType().unitWeightValue;
    double unitValueB = getItemB().getAmount() * getItemB().getType().unitWeightValue;
    return isSameUnitCategory()
        &&
        unitValueA == unitValueB ? 1 : 0;
  }

  private boolean isSameUnitCategory() {
    return getItemA().getType().category.equals(getItemB().getType().category);
  }

}
