package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.users.RefreshToken;
import be.xplore.pricescraper.entity.users.RefreshTokenEntity;
import be.xplore.pricescraper.repositories.RefreshTokenRepository;
import be.xplore.pricescraper.repositories.jpa.RefreshTokenJpaRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of repository.
 */
@Repository
@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
  private ModelMapper modelMapper;
  private RefreshTokenJpaRepository repository;

  /**
   * Find by primary key.
   */
  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return repository.findByTokenIgnoreCase(token)
        .map(e -> modelMapper.map(e, RefreshToken.class));
  }

  /**
   * Delete token.
   */
  public void deleteToken(String previousToken) {
    repository.deleteByTokenIgnoreCase(previousToken);
  }

  /**
   * Save entity.
   */
  public RefreshToken save(RefreshToken token) {
    return modelMapper
        .map(repository
            .save(modelMapper
                .map(token, RefreshTokenEntity.class)), RefreshToken.class);
  }
}
