package be.xplore.pricescraper.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.domain.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTests {
  private User user;

  @BeforeEach
  void prepare() {
    user = new User("Username", "Github");
  }

  @Test
  void getters() {
    assertNull(user.getId());
    assertNotNull(user.getUsername());
    assertNotNull(user.getProvider());
    assertNull(user.getPassword());
    assertTrue(user.isAccountNonExpired());
    assertTrue(user.isEnabled());
    assertTrue(user.isAccountNonLocked());
    assertTrue(user.isCredentialsNonExpired());
    assertNull(user.getAuthorities());
  }
}
