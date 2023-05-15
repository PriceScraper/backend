package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.factories.ItemMatcherCombinerFactory;
import be.xplore.pricescraper.matchers.combiners.WeightedItemMatcherCombiner;
import be.xplore.pricescraper.utils.matchers.Matcher;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Implementation for {@link ItemMatcherCombinerFactory}.
 */
@Component
public class ItemMatcherCombinerFactoryImpl implements ItemMatcherCombinerFactory {
  @Override
  public Matcher makeWeightedCombiner(String name, List<Matcher> matchers,
                                      List<Integer> weights) {
    return switch (name.toLowerCase()) {
      case "weighted" -> {
        WeightedItemMatcherCombiner combiner = new WeightedItemMatcherCombiner(0.7);
        for (int i = 0; i < matchers.size(); i++) {
          combiner.addMatcher((ItemMatcher) matchers.get(i), weights.get(i));
        }
        yield combiner;
      }
      default -> throw new IllegalArgumentException(
          "Error on creating matcher combiner, name does not exist.");
    };
  }
}
