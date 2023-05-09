package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.users.UserEntity;
import be.xplore.pricescraper.repositories.UserRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * JPA repository implementation of {@link UserRepository}.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
  @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.shoppingLists WHERE u.username = :login")
  Optional<UserEntity> findByUsernameWithShoppingLists(@Param("login") String login);

  Optional<UserEntity> findByUsernameAndProvider(String login, String github);
}
