package be.xplore.pricescraper.dtos;

/**
 * This record defines an Object response from a service including extra information.
 */
public record ServiceResponse<T>(boolean isSuccessful, T object, String message) {
}
