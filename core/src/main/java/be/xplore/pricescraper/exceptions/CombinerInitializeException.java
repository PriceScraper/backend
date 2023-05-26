package be.xplore.pricescraper.exceptions;

import be.xplore.pricescraper.matchers.Combiner;

/**
 * Exception thrown when a {@link Combiner} was not initialized correctly.
 */
public class CombinerInitializeException extends RuntimeException {

  public CombinerInitializeException() {
  }

  public CombinerInitializeException(String message) {
    super(message);
  }
}
