package be.xplore.pricescraper.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.xplore.pricescraper.RestTestConfig;
import be.xplore.pricescraper.controllers.advice.UserActionExceptionHandler;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {ShoppingListsController.class, RestTestConfig.class,
    UserActionExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class ShoppingListControllerTests {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  UserService userService;
  @Autowired
  ShoppingListsController shoppingListsController;

  @Test
  @WithMockUser("test user")
  void deletingShouldFail() throws Exception {
    when(userService.loadUserByUsername(any(String.class))).thenReturn(
        new User("test", "provider"));
    mockMvc.perform(
            delete("/shoppinglists/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

}
