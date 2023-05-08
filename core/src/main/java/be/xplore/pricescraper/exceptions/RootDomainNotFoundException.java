package be.xplore.pricescraper.exceptions;

import be.xplore.pricescraper.scrapers.ItemScraper;

/**
 * Exception gets thrown when the domain if a {@link ItemScraper} can't be determined.
 */
public class RootDomainNotFoundException extends RuntimeException {
  public RootDomainNotFoundException() {
    super();
  }

  public RootDomainNotFoundException(String message) {
    super(message);
  }
}
