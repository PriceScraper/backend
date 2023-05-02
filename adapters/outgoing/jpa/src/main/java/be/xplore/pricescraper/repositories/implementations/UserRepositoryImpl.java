package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.entity.users.UserEntity;
import be.xplore.pricescraper.repositories.UserRepository;
import be.xplore.pricescraper.repositories.jpa.UserJpaRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of JPA repository.
 */
@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserJpaRepository jpaRepository;
  private final ModelMapper modelMapper;

  /**
   * Find user by account name & provider.
   */
  @Override
  public Optional<User> findByUsernameAndProvider(String login, String provider) {
    return jpaRepository.findByUsernameAndProvider(login, provider)
        .map(entity -> modelMapper.map(entity, User.class));
  }

  /**
   * Find user by account with shopping lists.
   */
  @Override
  public Optional<User> findByUsernameWithShoppingLists(String login) {
    var entity = jpaRepository.findByUsernameWithShoppingLists(login);
    return entity.map(userEntity -> modelMapper.map(userEntity, User.class));
  }

  /**
   * Save entity.
   */
  @Override
  public User save(User user) {
    var entity = modelMapper.map(user, UserEntity.class);
    entity = jpaRepository.save(entity);
    return modelMapper.map(entity, User.class);
  }

  /**
   * Find by key.
   */
  @Override
  public Optional<User> findById(long userId) {
    var entity = jpaRepository.findById(userId);
    return entity.map(userEntity -> modelMapper.map(userEntity, User.class));
  }
}
