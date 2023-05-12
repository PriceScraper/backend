package be.xplore.pricescraper.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTests {
  private User user;

  @BeforeEach
  void prepare() {
    user = new User("Username", "Github", "avatar");
  }

  @Test
  void getters() {
    assertThat(user.getId()).isNull();
    assertThat(user.getUsername()).isNotNull();
    assertThat(user.getProvider()).isNotNull();
    assertThat(user.getPassword()).isNull();
    assertThat(user.isAccountNonExpired()).isTrue();
    assertThat(user.isEnabled()).isTrue();
    assertThat(user.isAccountNonLocked()).isTrue();
    assertThat(user.isCredentialsNonExpired()).isTrue();
    assertThat(user.getAuthorities()).isEmpty();
  }
}
