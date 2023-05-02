package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.users.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link User}.
 */
@Repository
public interface UserRepository {
  Optional<User> findByUsernameAndProvider(String login, String github);

  Optional<User> findByUsernameWithShoppingLists(String login);

  User save(User user);

  Optional<User> findById(long userId);

}
