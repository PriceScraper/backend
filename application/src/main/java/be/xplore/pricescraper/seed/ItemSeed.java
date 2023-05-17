package be.xplore.pricescraper.seed;

import be.xplore.pricescraper.services.ItemService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Seed of items.
 */
@Profile("!test")
@AllArgsConstructor
@Slf4j
@Component
public class ItemSeed {
  private final ItemService itemService;

  /**
   * Executing seed.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void seedItems() {
    if (itemService.trackedItemsCount() > 0) {
      log.info("Not seeding because db is not empty.");
      return;
    }
    log.info("Seeding items.");
    var discoverItems = new String[] {
        "pizza", "melk", "bananen", "jupiler"
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
}
