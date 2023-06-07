package be.xplore.pricescraper.seed;

import be.xplore.pricescraper.services.ItemService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ItemSeed implements Seed {
  private final ItemService itemService;

  public void execute() {
    log.info("Discovering pre-defined items.");
    var discoverItems = new String[] {
        "Dr. oetker pizza", "spinaci", "tonno", "spekblokjes",
        "linguine", "parmigiano", "Barilla", "water"
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
