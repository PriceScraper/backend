package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.users.RefreshToken;
import java.util.Optional;

/**
 * Necessary methods to handle refresh tokens at db level.
 */
public interface RefreshTokenRepository {
  Optional<RefreshToken> findByToken(String token);

  void deleteToken(String previousToken);

  RefreshToken save(RefreshToken token);
}
