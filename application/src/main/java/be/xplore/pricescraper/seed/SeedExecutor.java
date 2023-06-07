package be.xplore.pricescraper.seed;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
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
public class SeedExecutor {
  private final List<Seed> seeds;

  /**
   * Executing seed.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void seed() {
    seeds.sort(Comparator.comparingInt(Seed::priority));
    seeds.forEach(Seed::execute);
  }
}
