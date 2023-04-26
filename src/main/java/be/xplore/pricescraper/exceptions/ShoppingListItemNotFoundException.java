package be.xplore.pricescraper.exceptions;

import be.xplore.pricescraper.domain.shops.Item;

/**
 * Exception gets thrown when the requested {@link Item} could not be found.
 * In contrast to the {@link ItemNotFoundException}, the
 * {@link Item} gets looked for in a
 * {@link be.xplore.pricescraper.domain.users.ShoppingList}.
 */
public class ShoppingListItemNotFoundException extends RuntimeException {
}
