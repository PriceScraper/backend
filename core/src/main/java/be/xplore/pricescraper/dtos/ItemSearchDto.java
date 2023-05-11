package be.xplore.pricescraper.dtos;

/**
 * Dto projection of a search of an {@link be.xplore.pricescraper.domain.shops.Item}.
 */

public record ItemSearchDto(int id, String name, String image) {
}
