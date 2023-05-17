package be.xplore.pricescraper.exceptions;

import be.xplore.pricescraper.domain.shops.TrackedItem;

/**
 * Exception thrown when matching of a {@link TrackedItem} fails.
 */
public class MatchException extends RuntimeException {

  public MatchException(String message) {
    super(message);
  }
}
