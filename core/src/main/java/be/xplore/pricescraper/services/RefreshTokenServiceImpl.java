package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.users.RefreshToken;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.UnauthorizedActionExeption;
import be.xplore.pricescraper.exceptions.UserNotFoundException;
import be.xplore.pricescraper.repositories.RefreshTokenRepository;
import be.xplore.pricescraper.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of RefreshTokenService.
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private RefreshTokenRepository tokenRepository;
  private UserRepository userRepository;

  /**
   * Implementation of interface.
   * Invalidate previous token.
   * Generate new token.
   */
  public RefreshToken refresh(String refreshToken) {
    var token = tokenRepository.findByToken(refreshToken);
    if (token.isEmpty()) {
      log.debug("Token, isEmpty: true");
      throw new UnauthorizedActionExeption();
    }
    if (hasExpired(token.get())) {
      log.debug("Token, hasExpired: true. Expiry: " + token.get().getExpiryDate());
      throw new UnauthorizedActionExeption();
    }
    tokenRepository.deleteToken(refreshToken);
    return createRefreshToken(token.get().getUser());
  }

  /**
   * Create from user.
   */
  private RefreshToken createRefreshToken(User user) {
    var token = new RefreshToken(user, generateTokenValue(user.getId()), getExpiryFromNow());
    return tokenRepository.save(token);
  }

  /**
   * Implementation of interface.
   * Generates token.
   */
  public RefreshToken createRefreshToken(long userId) {
    var user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new UserNotFoundException();
    }
    return createRefreshToken(user.get());
  }

  /**
   * Generate token string.
   */
  private String generateTokenValue(long userId) {
    return userId + "-" + UUID.randomUUID();
  }

  /**
   * Generate Instant to define expiry date.
   */
  private Instant getExpiryFromNow() {
    return getInstantNow()
        .plusSeconds(300);
  }

  /**
   * Has expired.
   */
  private boolean hasExpired(RefreshToken refreshToken) {
    log.debug("Expiry: " + refreshToken.getExpiryDate() + ", Current time: " + getInstantNow());
    return refreshToken.getExpiryDate().compareTo(getInstantNow()) < 0;
  }

  /**
   * Get instant from now.
   */
  private Instant getInstantNow() {
    return Instant.now()
        .atZone(ZoneId.of("Europe/Paris"))
        .toInstant();
  }
}
