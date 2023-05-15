package be.xplore.pricescraper.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.utils.matchers.Matcher;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ItemMatcherCombinerFactoryTests {
  private static final ItemMatcherCombinerFactoryImpl factory =
      new ItemMatcherCombinerFactoryImpl();

  @Test
  void shouldCreateCombiner() {
    Matcher matcher = factory.makeWeightedCombiner("weighted", List.of(), List.of());
    assertThat(matcher).isNotNull();
  }

  @Test
  void shouldThrow() {
    assertThatThrownBy(
        () -> factory.makeWeightedCombiner("bla", new ArrayList<>(),
            new ArrayList<>())).isInstanceOf(
        IllegalArgumentException.class);

  }

}
