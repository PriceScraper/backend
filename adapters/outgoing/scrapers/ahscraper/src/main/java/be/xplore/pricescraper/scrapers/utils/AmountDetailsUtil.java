package be.xplore.pricescraper.scrapers.utils;

import be.xplore.pricescraper.domain.shops.UnitType;
import be.xplore.pricescraper.dtos.ItemAmountDetails;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * Extract amount details from String. For ah.be scraper.
 */
@Slf4j
public class AmountDetailsUtil {
  private static final String itemUnitRegexGroup = "(cl|l|ml|kg|gram|g)";

  /**
   * Execution.
   */
  public static Optional<ItemAmountDetails> extractFromString(String title) {
    try {
      var lowerTitle = title.toLowerCase();
      if (lowerTitle.contains("stuks")) {
        return amountOnlyV2(lowerTitle);
      }
      var v1 = quantityAndAmount(lowerTitle);
      if (v1.isPresent()) {
        return v1;
      }
      var v2 = amountOnlyV1(lowerTitle);
      if (v2.isPresent()) {
        return v2;
      }
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return Optional.empty();
  }

  private static Optional<ItemAmountDetails> quantityAndAmount(String title) {
    var patternWithQuantity1 =
        Pattern.compile(
            "([0-9 ]{1,8}) x ([0-9., ]{1,8}) " + itemUnitRegexGroup);
    var matherQuantity1 = patternWithQuantity1.matcher(title.toLowerCase());
    if (!matherQuantity1.matches()) {
      return Optional.empty();
    }
    return Optional.of(new ItemAmountDetails(
        stringToType(matherQuantity1.group(3)),
        parseDouble(matherQuantity1.group(2)),
        Integer.parseInt(matherQuantity1.group(1).strip())));
  }

  private static Optional<ItemAmountDetails> amountOnlyV1(String title) {
    var patternWithQuantity1 =
        Pattern.compile(
            "([0-9., ]{1,8}) " + itemUnitRegexGroup);
    var matherQuantity1 = patternWithQuantity1.matcher(title.toLowerCase());
    if (!matherQuantity1.matches()) {
      return Optional.empty();
    }
    return Optional.of(new ItemAmountDetails(
        stringToType(matherQuantity1.group(2)),
        parseDouble(matherQuantity1.group(1)),
        1));
  }

  private static Optional<ItemAmountDetails> amountOnlyV2(String title) {
    var patternWithQuantity1 =
        Pattern.compile(
            "([0-9 ]{1,8}) stuks");
    var matherQuantity1 = patternWithQuantity1.matcher(title.toLowerCase());
    if (!matherQuantity1.matches()) {
      return Optional.empty();
    }
    return Optional.of(new ItemAmountDetails(
        UnitType.not_available,
        Integer.parseInt(matherQuantity1.group(1).strip()),
        1));
  }

  private static UnitType stringToType(String val) {
    val = val.toLowerCase();
    if (val.equals("gram")) {
      val = "g";
    }
    return UnitType.valueOf(val);
  }

  private static double parseDouble(String val) {
    return Double.parseDouble(val.strip().replace(",", "."));
  }
}
