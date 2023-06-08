package be.xplore.pricescraper.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.xplore.pricescraper.RestTestConfig;
import be.xplore.pricescraper.controllers.advice.UserActionExceptionHandler;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.services.UserService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {ShoppingListsController.class, RestTestConfig.class,
    UserActionExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ShoppingListControllerTests {
  @MockBean
  UserService userService;
  @Autowired
  ShoppingListsController shoppingListsController;
  @Autowired
  private MockMvc mockMvc;
  private final User testUser;

  public ShoppingListControllerTests() {
    this.testUser = new User();
    this.testUser.setId(1L);
    this.testUser.setShoppingLists(new ArrayList<>());
    this.testUser.setAvatar("avatar");
    this.testUser.setProvider("github");
    this.testUser.setUsername("test");
  }

  @BeforeEach
  void prepare() {
    when(userService.loadUserByUsername(anyString()))
        .thenReturn(testUser);
  }

  @Test
  @WithMockUser("test user")
  void deletingShouldFail() throws Exception {
    mockMvc.perform(
            delete("/shoppinglists/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser("test user")
  void deleteRecurring() throws Exception {
    mockMvc.perform(
            delete("/shoppinglists/items/remove/recurring/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser("test user")
  void getShoppingListsForUser() throws Exception {
    mockMvc.perform(
            get("/shoppinglists")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

}
