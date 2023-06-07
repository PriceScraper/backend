package be.xplore.pricescraper.seed;

import java.util.Comparator;
import java.util.List;
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
@Component
@Slf4j
public class SeedExecutor {
  private final List<Seed> seeders;

  /**
   * Executing seed.
   */
  @EventListener(ApplicationReadyEvent.class)
  public boolean seed() {
    try {
      sort();
      seeders.forEach(Seed::execute);
      return true;
    } catch (Exception e) {
      log.error("Seed error: " + e.getMessage());
    }
    return false;
  }

  public List<Seed> getSeeders() {
    return seeders;
  }

  public void sort() {
    seeders.sort(Comparator.comparingInt(Seed::priority));
  }
}
