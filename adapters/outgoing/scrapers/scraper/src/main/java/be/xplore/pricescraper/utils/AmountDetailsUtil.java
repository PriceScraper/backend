package be.xplore.pricescraper.utils;

import be.xplore.pricescraper.domain.shops.UnitType;
import be.xplore.pricescraper.dtos.ItemAmountDetails;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * Extract amount details from String.
 */
@Slf4j
public class AmountDetailsUtil {
  private static final String ITEM_NAME_REGEX_GROUP = "([a-z -.%]+)";
  private static final String ITEM_UNIT_REGEX_GROUP = "(cl|l|ml|kg|g)";

  /**
   * Execution.
   */
  public static Optional<ItemAmountDetails> extractFromString(String title) {
    try {
      var lowerTitle = title.toLowerCase();
      var withV1 = withQuantityV1(lowerTitle);
      if (withV1.isPresent()) {
        return withV1;
      }
      var withV2 = withQuantityV2(lowerTitle);
      if (withV2.isPresent()) {
        return withV2;
      }
      return withoutQuantity(lowerTitle);
    } catch (Exception e) {
      log.warn(e.toString());
      return Optional.empty();
    }
  }

  private static Optional<ItemAmountDetails> withQuantityV1(String title) {
    var patternWithQuantity1 =
        Pattern.compile(
            ITEM_NAME_REGEX_GROUP
                + "([0-9. ]{1,8})"
                + ITEM_UNIT_REGEX_GROUP
                + "( ?)x( ?)([0-9 ]{1,8})");
    var matherQuantity1 = patternWithQuantity1.matcher(title.toLowerCase());
    if (!matherQuantity1.matches()) {
      return Optional.empty();
    }
    return Optional.of(new ItemAmountDetails(
        UnitType.valueOf(matherQuantity1.group(3)),
        Double.parseDouble(matherQuantity1.group(2).strip()),
        Integer.parseInt(matherQuantity1.group(6).strip())));
  }

  private static Optional<ItemAmountDetails> withQuantityV2(String title) {
    var patternWithQuantity2 =
        Pattern.compile(
            ITEM_NAME_REGEX_GROUP
                + "([0-9 ]{1,8})( ?)x( ?)([0-9. ]{1,8})"
                + ITEM_UNIT_REGEX_GROUP
                + "?");
    var matherQuantity2 = patternWithQuantity2.matcher(title.toLowerCase());
    if (!matherQuantity2.matches()) {
      return Optional.empty();
    }
    return Optional.of(new ItemAmountDetails(
        UnitType.valueOf(matherQuantity2.group(6)),
        Double.parseDouble(matherQuantity2.group(5).strip()),
        Integer.parseInt(matherQuantity2.group(2).strip())));

  }

  private static Optional<ItemAmountDetails> withoutQuantity(String title) {
    var patternNoQuantity =
        Pattern.compile(ITEM_NAME_REGEX_GROUP
            + "([0-9 .]{1,8})"
            + ITEM_UNIT_REGEX_GROUP);
    var matherNoQuantity = patternNoQuantity.matcher(title.toLowerCase());
    if (!matherNoQuantity.matches()) {
      return Optional.empty();
    }
    return Optional.of(new ItemAmountDetails(UnitType.valueOf(matherNoQuantity.group(3)),
        Double.parseDouble(matherNoQuantity.group(2).strip()), 1));
  }
}
