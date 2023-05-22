package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.utils.matchers.Combiner;
import be.xplore.pricescraper.utils.matchers.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ItemMatcherCombinerFactoryImpl.class, Matcher.class})
class ItemMatcherCombinerFactoryTests {

  @Autowired
  private ItemMatcherCombinerFactoryImpl factory;

  @Test
  void shouldCreateCombiner() {
    Combiner combiner = factory.makeWeightedCombiner();
    assertThat(combiner).isNotNull();
  }

}
