package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.UserNotFoundException;
import be.xplore.pricescraper.repositories.UserRepository;
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

  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsernameWithShoppingLists(username)
        .orElseThrow(UserNotFoundException::new);
  }
}
