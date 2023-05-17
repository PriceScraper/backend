package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.factories.ItemMatcherCombinerFactory;
import be.xplore.pricescraper.matchers.combiners.WeightedItemMatcherCombiner;
import be.xplore.pricescraper.utils.matchers.Combiner;
import be.xplore.pricescraper.utils.matchers.Matcher;
import java.util.List;
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
  public Combiner makeWeightedCombiner() {
    WeightedItemMatcherCombiner combiner = new WeightedItemMatcherCombiner();
    double weightPerMatcher = (double) 1 / matchers.size();
    for (int i = 0; i < matchers.size(); i++) {
      combiner.addMatcher((ItemMatcher) matchers.get(i), weightPerMatcher);
    }
    return combiner;
  }
}
