package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.users.RefreshToken;
import org.springframework.stereotype.Service;

/**
 * Providing functionalities to handle Refresh tokens.
 */
@Service
public interface RefreshTokenService {
  /**
   * Invalidate refreshToken and return new one.
   */
  RefreshToken refresh(String refreshToken);

  /**
   * Create refresh token from userId.
   */

  RefreshToken createRefreshToken(long userId);

}
