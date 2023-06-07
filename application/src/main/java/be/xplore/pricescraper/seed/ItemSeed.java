package be.xplore.pricescraper.seed;

import be.xplore.pricescraper.services.ItemService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Seeding a range of items too make sure out database is filled.
 */
@Component
@Slf4j
@AllArgsConstructor
public class ItemSeed implements Seed {
  private final ItemService itemService;

  /**
   * Seed executor.
   */
  public void execute() {
    log.info("Discovering pre-defined items.");
    var discoverItems = new String[] {
        "Dr. oetker pizza", "Dr. oetker", "spinaci", "tonno",
        "linguine", "parmigiano", "Barilla",
        "water", "fruit", "groenten",
        "spekblokjes", "chocolade", "jupiler",
        "stella artois", "bananen", "pizza"
    };

    Arrays.stream(discoverItems).toList()
        .forEach(i -> {
          try {
            itemService.discoverNewItems(i);
            log.debug("Discovered items for " + i);
          } catch (Exception e) {
            log.error(e.getMessage() + " while discovering query " + i);
          }
        });
  }

  @Override
  public int priority() {
    return 2;
  }
}
