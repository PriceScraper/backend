package be.xplore.pricescraper.factories;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.utils.matchers.Matcher;

/**
 * Factory for creating Item Matchers.
 */
public interface ItemMatcherFactory {
  Matcher makeByNameAndItems(String matcherName, Item itemA, Item itemB);
}
