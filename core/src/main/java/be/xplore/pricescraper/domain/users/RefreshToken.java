package be.xplore.pricescraper.domain.users;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Refresh token entity, to extend JWT sessions.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
  private User user;
  private String token;
  private Instant expiryDate;
}
