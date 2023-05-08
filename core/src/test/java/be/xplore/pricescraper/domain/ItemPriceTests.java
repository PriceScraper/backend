package be.xplore.pricescraper.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.xplore.pricescraper.domain.shops.ItemPrice;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class ItemPriceTests {
  @Test
  void constructor() {
    var entity = new ItemPrice();
    assertNotNull(entity);
  }

  @Test
  void settersAndGetters() {
    var entity = new ItemPrice();

    assertEquals(0, entity.getPrice());
    entity.setPrice(5);
    assertEquals(5, entity.getPrice());

    assertEquals(0, entity.getId());
    entity.setId(5);
    assertEquals(5, entity.getId());

    var now = Instant.now();
    assertNull(entity.getTimestamp());
    entity.setTimestamp(now);
    assertEquals(now, entity.getTimestamp());
  }
}
