package be.xplore.pricescraper.scrapers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.domain.shops.UnitType;
import be.xplore.pricescraper.utils.AmountDetailsUtil;
import org.junit.jupiter.api.Test;

class AmountDetailsUtilTests {
  @Test
  void unitBeforeX() {
    var res = AmountDetailsUtil.extractFromString("Chaudfontaine Sparkling Pet 1500 ml x 6");
    assertTrue(res.isPresent());
    assertEquals(1500, res.get().amount());
    assertEquals(UnitType.ML, res.get().type());
    assertEquals(6, res.get().quantity());
  }

  @Test
  void unitAfterX() {
    var res = AmountDetailsUtil.extractFromString("Chaudfontaine Sparkling Pet 12x1500cl");
    assertTrue(res.isPresent());
    assertEquals(1500, res.get().amount());
    assertEquals(UnitType.CL, res.get().type());
    assertEquals(12, res.get().quantity());
  }

  @Test
  void noQuantity() {
    var res = AmountDetailsUtil.extractFromString("Chaudfontaine Sparkling Pet 1.5L");
    assertTrue(res.isPresent());
    assertEquals(1.5, res.get().amount());
    assertEquals(UnitType.L, res.get().type());
    assertEquals(1, res.get().quantity());
  }
}
