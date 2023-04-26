package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.users.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * JPA Queries for User Repository.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsernameAndProvider(String login, String github);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.shoppingLists WHERE u.username = :login")
  Optional<User> findByUsernameWithShoppingLists(@Param("login") String login);

}
