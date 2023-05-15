package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.users.RefreshTokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa repository.
 */
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, String> {
  /**
   * Find by PK.
   */
  Optional<RefreshTokenEntity> findByTokenIgnoreCase(String token);

  /**
   * Delete by PK.
   */
  void deleteByTokenIgnoreCase(String token);

}
