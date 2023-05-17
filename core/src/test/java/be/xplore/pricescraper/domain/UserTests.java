package be.xplore.pricescraper.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.users.RecurringShoppingListItem;
import be.xplore.pricescraper.domain.users.ShoppingListLine;
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

  @Test
  void shoppingListLine() {
    var entity = new ShoppingListLine(1, 2, new Item());
    assertNotNull(entity);
    assertNotNull(entity.getItem());
    assertEquals(1, entity.getId());
    assertEquals(2, entity.getQuantity());

    entity = new ShoppingListLine(2, new Item());
    assertNotNull(entity);
    assertNotNull(entity.getItem());
    assertEquals(0, entity.getId());
    assertEquals(2, entity.getQuantity());
  }

  @Test
  void recurringShoppingListItem() {
    var entity = new RecurringShoppingListItem(1, new Item(), 2, new User());
    assertNotNull(entity);
    assertNotNull(entity.getItem());
    assertNotNull(entity.getUser());
    assertEquals(1, entity.getId());
    assertEquals(2, entity.getQuantity());

    entity = new RecurringShoppingListItem(new Item(), 2, new User());
    assertNotNull(entity);
    assertNotNull(entity.getItem());
    assertNotNull(entity.getUser());
    assertEquals(0, entity.getId());
    assertEquals(2, entity.getQuantity());
  }
}
