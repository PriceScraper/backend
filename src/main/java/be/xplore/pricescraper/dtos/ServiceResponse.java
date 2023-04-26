package be.xplore.pricescraper.dtos;

/**
 * Returning additional information from the services to the controllers.
 * Handy for return status codes and displaying errors.
 */
public record ServiceResponse<T>(boolean hasSucceeded, T object, String message) {
}
