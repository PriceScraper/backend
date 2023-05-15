package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.factories.ItemMatcherFactory;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Implementation for {@link ItemMatcherFactory}.
 */
@AllArgsConstructor
@Component
public class ItemMatcherFactoryImpl implements ItemMatcherFactory {

  private Environment env;

  /**
   * Make {@link ItemMatcher} by name and items.
   */
  public ItemMatcher makeByNameAndItems(String matcherName, Item itemA, Item itemB) {
    return switch (matcherName.toLowerCase()) {
      case "ingredient" -> new IngredientMatcher(50, itemA, itemB);
      case "imagerecognition" -> new ImageRecognitionMatcher(20, itemA, itemB, env);
      default ->
          throw new IllegalArgumentException("Error on creating matcher, name does not exist.");
    };
  }

}
