package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.users.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for {@link User}s.
 */
@Service
public interface UserService {
  User loadUserByUsername(String username) throws UsernameNotFoundException;
}
