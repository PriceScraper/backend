package be.xplore.pricescraper.matchers.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MatchStringUtilsTests {

  @Test
  void stringShouldBeNormalized() {
    String normalizedString = MatchStringUtils.normalizeString("Eieren.,");
    assertThat(normalizedString).isEqualTo("ei");
  }

}
