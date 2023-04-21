package be.xplore.pricescraper.repository;

import be.xplore.pricescraper.domain.users.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Queries for User Repository.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsernameAndProvider(String login, String github);
}
