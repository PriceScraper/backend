package be.xplore.pricescraper.dtos;

/**
 * To transfer both tokens.
 */
public record RefreshAndAccessTokenDto(String refreshToken, String accessToken) {
}
