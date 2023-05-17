package be.xplore.pricescraper.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.domain.shops.Item;
import org.junit.jupiter.api.Test;

public class RecurringItemTests {
  @Test
  void constructorAddRecurringItemDto() {
    var add = new AddRecurringItemDto(1, 2);
    assertNotNull(add);
    assertEquals(1, add.itemId());
    assertEquals(2, add.quantity());
  }

  @Test
  void constructorRecurringItemDto() {
    var dto = new RecurringItemDto(1, new Item(), 2);
    assertNotNull(dto);
    assertEquals(1, dto.id());
    assertNotNull(dto.item());
    assertEquals(2, dto.quantity());
  }
}
