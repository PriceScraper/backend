package be.xplore.pricescraper.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BarcodeScanResultTests {
  @Test
  void constructor() {
    var entity = new BarcodeScanResult(true, "title");
    assertNotNull(entity);
    assertTrue(entity.succeeded());
    assertEquals("title", entity.result());
  }
}
