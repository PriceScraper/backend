package be.xplore.pricescraper.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import org.junit.jupiter.api.Test;

class BarcodeItemServiceImplTests {
  private final BarcodeItemServiceImpl service;

  public BarcodeItemServiceImplTests() {
    this.service = new BarcodeItemServiceImpl();
  }

  @Test
  void getProductNameValid() {
    var response = service.getProductName("5000112638783");
    assertEquals("fanta orange", response.toLowerCase());
  }

  @Test
  void getProductNameNotFound() {
    assertThrows(ItemNotFoundException.class, () -> {
      service.getProductName("5");
    });
  }

  @Test
  void cleanName() {
    assertEquals("Milk", service.cleanProductName("Milk"));
    assertEquals("Milk", service.cleanProductName("Milk 5l"));
    assertEquals("Milk", service.cleanProductName("Milk 5cl"));
    assertEquals("Milk", service.cleanProductName("Milk 5ml"));
    assertEquals("Milk", service.cleanProductName("Milk 51ml"));
    assertEquals("Milk", service.cleanProductName("Milk 517cl"));
    assertEquals("Milk", service.cleanProductName(" Milk 517cl"));
  }
}
