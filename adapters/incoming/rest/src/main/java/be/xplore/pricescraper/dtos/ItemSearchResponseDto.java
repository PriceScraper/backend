package be.xplore.pricescraper.dtos;

/**
 * Dto that contains the response for an {@link be.xplore.pricescraper.domain.shops.Item} search.
 */
public record ItemSearchResponseDto(int id, String name, String image) {
}
