package be.xplore.pricescraper.exceptions;

import be.xplore.pricescraper.domain.users.RecurringShoppingListItem;

/**
 * Exception thrown when {@link RecurringShoppingListItem} was not found.
 */
public class RecurringItemNotFoundException extends RuntimeException {
  public RecurringItemNotFoundException(String message) {
    super(message);
  }
}
