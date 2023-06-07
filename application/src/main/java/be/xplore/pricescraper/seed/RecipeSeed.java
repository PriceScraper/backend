package be.xplore.pricescraper.seed;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.recipes.RecipeItem;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.UserRepository;
import be.xplore.pricescraper.services.ItemService;
import be.xplore.pricescraper.services.RecipeService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RecipeSeed implements Seed {
  private ItemService itemService;
  private UserRepository userRepository;
  private RecipeService recipeService;

  @Override
  public void execute() {
    var user = getUser();
    if(user == null) {
      log.warn("Failed to seed recipes, no user found.");
      return;
    }
    if(!recipeService.getByCreator(user).isEmpty()) {
      log.info("Recipe repository has already been seeded.");
      return;
    }
    log.info("Seeding recipes.");
    var recipe = recipeService.add("Spaghetti Carbonara", user);
    var pasta = tryGetItem("Barilla Linguine");
    pasta.ifPresent(
        itemSearchDto
            -> recipeService.addItemToRecipe(recipe.getId(), itemSearchDto.getId(), user));
    var cheese = tryGetItem("Parmareggio Parmigiano Reggiano");
    cheese.ifPresent(
        itemSearchDto
            -> recipeService.addItemToRecipe(recipe.getId(), itemSearchDto.getId(), user));
    var bacon = tryGetItem("Herta Spekblokjes");
    bacon.ifPresent(
        itemSearchDto
            -> recipeService.addItemToRecipe(recipe.getId(), itemSearchDto.getId(), user));
    var eggs = tryGetItem("Scharreleieren");
    eggs.ifPresent(
        itemSearchDto
            -> recipeService.addItemToRecipe(recipe.getId(), itemSearchDto.getId(), user));
  }

  private Optional<ItemSearchDto> tryGetItem(String name) {
    var potential = itemService.findItemByNameLike(name);
    if (potential.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(potential.get(0));
  }

  private User getUser() {
    return userRepository.findByUsernameAndProvider("Bob", "Seed").orElse(null);
  }

  @Override
  public int priority() {
    return 1;
  }
}
