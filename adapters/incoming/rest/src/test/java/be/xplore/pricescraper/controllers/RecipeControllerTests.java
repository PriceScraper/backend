package be.xplore.pricescraper.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.xplore.pricescraper.RestTestConfig;
import be.xplore.pricescraper.controllers.advice.UserActionExceptionHandler;
import be.xplore.pricescraper.dtos.CreateRecipeDto;
import be.xplore.pricescraper.dtos.ModifyRecipeItemDto;
import be.xplore.pricescraper.services.RecipeService;
import be.xplore.pricescraper.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {RecipeController.class, RestTestConfig.class,
    UserActionExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerTests {

  @MockBean
  UserService userService;
  @MockBean
  RecipeService recipeService;
  @Autowired
  RecipeController shoppingListsController;
  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser("test user")
  void getPersonalRecipesShouldReturnOk() throws Exception {
    mockMvc.perform(
            get("/recipe/personal")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser("test user")
  void addRecipeShouldReturnOk() throws Exception {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(new CreateRecipeDto("test"));
    mockMvc.perform(
            post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser("test user")
  void getByTitleShouldReturnOk() throws Exception {
    mockMvc.perform(
            get("/recipe?filter=test")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser("test user")
  void getByIdShouldReturnNotFound() throws Exception {
    mockMvc.perform(
            get("/recipe/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser("test user")
  void addItemToRecipeShouldReturnNotFound() throws Exception {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(new ModifyRecipeItemDto(1, 1));
    mockMvc.perform(
            post("/recipe/item/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser("test user")
  void removeItemToRecipeShouldReturnNotFound() throws Exception {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(new ModifyRecipeItemDto(1, 1));
    mockMvc.perform(
            post("/recipe/item/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andDo(print())
        .andExpect(status().isNotFound());
  }

}
