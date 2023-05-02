package be.xplore.pricescraper.dtos;

/**
 * Dto for modifying items in a {@link be.xplore.pricescraper.domain.users.ShoppingList}.
 */
public record ShoppingListItemDto(int shoppingListId, int itemId, int quantity) {
}
