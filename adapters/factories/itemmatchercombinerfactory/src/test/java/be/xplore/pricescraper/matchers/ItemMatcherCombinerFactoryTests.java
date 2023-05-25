package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.exceptions.CombinerInitializeException;
import be.xplore.pricescraper.matchers.factories.ItemMatcherCombinerFactoryImpl;
import be.xplore.pricescraper.utils.matchers.Combiner;
import be.xplore.pricescraper.utils.matchers.Matcher;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ItemMatcherCombinerFactoryImpl.class, Matcher.class})
class ItemMatcherCombinerFactoryTests {

  @Autowired
  private ItemMatcherCombinerFactoryImpl factory;

  @Test
  void shouldCreateCombiner() {
    Map<Matcher, Double> matchersWithWeights = new HashMap<>();
    matchersWithWeights.put(new IngredientMatcher(), 1.0);
    Combiner combiner = factory.makeWeightedCombiner(matchersWithWeights);
    assertThat(combiner).isNotNull();
  }

  @Test
  void shouldFail() {
    Map<Matcher, Double> matchersWithWeights = new HashMap<>();
    matchersWithWeights.put(new IngredientMatcher(), 0.1);
    assertThatThrownBy(() -> factory.makeWeightedCombiner(matchersWithWeights)).isInstanceOf(
        CombinerInitializeException.class);
  }

}
