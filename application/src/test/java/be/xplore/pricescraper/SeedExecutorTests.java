package be.xplore.pricescraper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.seed.Seed;
import be.xplore.pricescraper.seed.SeedExecutor;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeedExecutorTests {
  private SeedExecutor executor;

  @BeforeEach
  void prepare() {
    var seeders = new ArrayList<Seed>();
    seeders.add(new MockedSeed(1, "A"));
    seeders.add(new MockedSeed(0, "B"));
    executor = new SeedExecutor(seeders);
  }

  @Test
  void sort() {
    assertEquals("A", ((MockedSeed) executor.getSeeders().get(0)).title());
    executor.sort();
    assertEquals("B", ((MockedSeed) executor.getSeeders().get(0)).title());
  }

  @Test
  void smoke() {
    var res = executor.seed();
    assertTrue(res);
  }
}

record MockedSeed(int priority, String title) implements Seed {

  @Override
  public void execute() {

  }
}