package be.xplore.pricescraper.matchers.factories;

import be.xplore.pricescraper.exceptions.CombinerInitializeException;
import be.xplore.pricescraper.factories.ItemMatcherCombinerFactory;
import be.xplore.pricescraper.matchers.ItemMatcher;
import be.xplore.pricescraper.matchers.combiners.WeightedItemMatcherCombiner;
import be.xplore.pricescraper.utils.matchers.Combiner;
import be.xplore.pricescraper.utils.matchers.Matcher;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation for {@link ItemMatcherCombinerFactory}.
 */
@AllArgsConstructor
@Component
public class ItemMatcherCombinerFactoryImpl implements ItemMatcherCombinerFactory {

  @Override
  public Combiner makeWeightedCombiner(Map<Matcher, Double> matchersWithWeights) {
    if (matchersWithWeights.values().stream().mapToDouble(Double::doubleValue).sum() != 1) {
      throw new CombinerInitializeException("Sum of weights should be 1");
    }
    WeightedItemMatcherCombiner combiner = new WeightedItemMatcherCombiner();
    for (Map.Entry<Matcher, Double> entry :
        matchersWithWeights.entrySet()) {
      combiner.addMatcher((ItemMatcher) entry.getKey(), entry.getValue());
    }
    return combiner;
  }
}
