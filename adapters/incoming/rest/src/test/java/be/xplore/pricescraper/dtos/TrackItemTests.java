package be.xplore.pricescraper.dtos;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class TrackItemTests {
  @Test
  void constructor() {
    var entity = new TrackItem("urlGoesHere");
    assertNotNull(entity);
    assertNotNull(entity.url());
  }
}
