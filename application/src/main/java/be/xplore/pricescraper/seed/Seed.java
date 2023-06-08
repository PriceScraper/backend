package be.xplore.pricescraper.seed;

import org.springframework.stereotype.Component;

/**
 * Interface for seed executors.
 */
@Component
public interface Seed {
  /**
   * Execute seed.
   */
  void execute();

  /**
   * Priority of seed class.
   */
  int priority();
}
