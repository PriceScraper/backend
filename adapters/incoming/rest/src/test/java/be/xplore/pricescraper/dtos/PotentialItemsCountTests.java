package be.xplore.pricescraper.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class PotentialItemsCountTests {
  @Test
  void constructor() {
    var entity = new PotentialItemsCount(2);
    assertNotNull(entity);
    assertEquals(2, entity.count());
  }
}
