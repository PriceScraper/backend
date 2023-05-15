package be.xplore.pricescraper.entity.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity of RefreshToken.
 */
@Entity(name = "RefreshToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {
  @Id
  @Column()
  private String token;
  @OneToOne
  @JoinColumn(name = "userId", referencedColumnName = "id")
  private UserEntity user;
  @Column()
  private Instant expiryDate;
}
