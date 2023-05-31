package be.xplore.pricescraper.exceptions;

import be.xplore.pricescraper.domain.shops.TrackedItem;

/**
 * This exception is thrown when the tracking of a {@link TrackedItem} fails.
 */
public class TrackItemException extends RuntimeException {
  public TrackItemException() {
    super();
  }
}
