package be.xplore.pricescraper.matchers;


import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.utils.matchers.Matcher;

/**
 * This is the abstract definition for an ItemMatcher that matches items from one or more sources.
 */
public abstract class ItemMatcher implements Matcher {

  private final Item itemA;
  private final Item itemB;

  public ItemMatcher(Item itemA, Item itemB) {
    this.itemA = itemA;
    this.itemB = itemB;
  }

  protected Item getItemA() {
    return itemA;
  }

  protected Item getItemB() {
    return itemB;
  }

  public abstract boolean isMatching();

}
