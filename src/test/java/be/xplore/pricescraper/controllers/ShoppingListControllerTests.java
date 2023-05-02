package be.xplore.pricescraper.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.xplore.pricescraper.config.SecurityConfig;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.services.ItemService;
import be.xplore.pricescraper.services.ShoppingListService;
import be.xplore.pricescraper.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShoppingListControllerTests {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  UserService userService;
  @InjectMocks
  ShoppingListsController shoppingListsController;
  @MockBean
  ItemService itemService;
  @MockBean
  ShoppingListService shoppingListService;

  private static TestingAuthenticationToken token;

  @BeforeAll
  void setup() {
    token =
        new TestingAuthenticationToken(new User("test", "provider"), null);
  }

  @Test
  @WithMockUser("test user")
  void deletingShouldFail() throws Exception {
    when(userService.loadUserByUsername(any(String.class))).thenReturn(
        new User("test", "provider"));
    mockMvc.perform(
            delete("/shoppinglists")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1}"))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

}
