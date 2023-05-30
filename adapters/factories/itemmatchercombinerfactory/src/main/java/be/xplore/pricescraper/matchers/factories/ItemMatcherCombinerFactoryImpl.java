package be.xplore.pricescraper.matchers.factories;

import be.xplore.pricescraper.exceptions.CombinerInitializeException;
import be.xplore.pricescraper.factories.ItemMatcherCombinerFactory;
import be.xplore.pricescraper.matchers.Combiner;
import be.xplore.pricescraper.matchers.Matcher;
import be.xplore.pricescraper.matchers.combiners.WeightedItemMatcherCombiner;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation for {@link ItemMatcherCombinerFactory}.
 */
@AllArgsConstructor
@Component
public class ItemMatcherCombinerFactoryImpl implements ItemMatcherCombinerFactory {

  private final List<Matcher> matchers;

  @Override
  public Combiner makeWeightedCombiner(Map<Matcher, Double> matchersWithWeights) {
    validateWeights(matchersWithWeights.values());
    WeightedItemMatcherCombiner combiner = new WeightedItemMatcherCombiner();
    for (Map.Entry<Matcher, Double> entry :
        matchersWithWeights.entrySet()) {
      combiner.addMatcher(entry.getKey());
      combiner.addWeightToMatcher(entry.getKey().getClass(), entry.getValue());
    }
    return combiner;
  }

  private void validateWeights(Collection<Double> weights) {
    if (getWeightSum(weights) != 1.0) {
      throw new CombinerInitializeException(
          "Matcher Combiner Weight should be 1 after calculating the sum");
    }
  }

  private double getWeightSum(Collection<Double> weights) {
    return weights.stream().mapToDouble(Double::doubleValue).sum();
  }

}
