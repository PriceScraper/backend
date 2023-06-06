package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.UserNotFoundException;
import be.xplore.pricescraper.repositories.UserRepository;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for {@link User}s.
 */
@RequiredArgsConstructor
@Service

public class UserServiceImpl implements UserDetailsService, UserService {

  private final UserRepository userRepository;

  /**
   * Gets user by username.
   */
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsernameWithShoppingLists(username)
        .orElseThrow(UserNotFoundException::new);
    user.getShoppingLists().forEach(shoppingList ->
        shoppingList.setLines(new ArrayList<>(new LinkedHashSet<>(shoppingList.getLines()))));
    return user;
  }
}
