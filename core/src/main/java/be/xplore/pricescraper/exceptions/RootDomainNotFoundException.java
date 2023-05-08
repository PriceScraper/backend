package be.xplore.pricescraper.exceptions;

public class RootDomainNotFoundException extends RuntimeException {
  public RootDomainNotFoundException() {
    super();
  }

  public RootDomainNotFoundException(String message) {
    super(message);
  }
}
