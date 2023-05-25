package be.xplore.pricescraper.dtos;

import be.xplore.pricescraper.domain.recipes.Recipe;

/**
 * Transfer object.
 */
public record ShoppingListFromRecipeCreateDto(String title, Recipe recipe) {
}
