package be.xplore.pricescraper.dtos;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ServiceResponseTests {
  @Test
  void constructor() {
    var entity = new ServiceResponse<>(true, "obj", "res");
    assertNotNull(entity);
  }
}
