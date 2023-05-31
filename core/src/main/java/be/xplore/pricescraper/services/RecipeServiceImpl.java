package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.recipes.RecipeItem;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.RecipeItemRepository;
import be.xplore.pricescraper.repositories.RecipeRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {
  private final RecipeRepository recipeRepository;
  private final ItemRepository itemRepository;
  private final RecipeItemRepository recipeItemRepository;

  @Override
  public Recipe add(String title, User user) {
    var e = new Recipe(title, user);
    return recipeRepository.save(e);
  }

  @Override
  public Optional<Recipe> get(long id) {
    var recipe = recipeRepository.findById(id).orElse(null);
    if (recipe == null) {
      return Optional.empty();
    }
    recipe.setItems(recipeItemRepository.findItemsByRecipeId(recipe.getId()));
    return Optional.of(recipe);
  }

  @Override
  public List<Recipe> getByCreator(User user) {
    return recipeRepository.findByCreatorId(user.getId());
  }

  @Override
  public List<Recipe> getByFilter(String filter) {
    return recipeRepository.findByTitle(filter);
  }

  @Override
  public Optional<Recipe> addItemToRecipe(long recipeId, int itemId, User user) {
    var res = get(recipeId).orElse(null);
    if (res == null) {
      return Optional.empty();
    }
    if (!res.getCreator().getId().equals(user.getId())) {
      return Optional.empty();
    }
    if (res.getItems().stream().anyMatch(e -> e.getItem().getId() == itemId)) {
      updateExistingItemInRecipe(res, itemId, 1);
    } else {
      addNewItemToRecipe(res, itemId);
    }
    return get(recipeId);
  }

  @Override
  public Optional<Recipe> removeItemFromRecipe(long recipeId, int itemId, User user) {
    var res = get(recipeId).orElse(null);
    if (res == null) {
      return Optional.empty();
    }
    if (!res.getCreator().getId().equals(user.getId())) {
      return Optional.empty();
    }
    updateExistingItemInRecipe(res, itemId, -1);
    return get(recipeId);
  }

  private void updateExistingItemInRecipe(Recipe res, int itemId, int valueChange) {
    var entity = recipeItemRepository.findItemByRecipeIdAndItemId(res.getId(), itemId)
        .orElseThrow(ItemNotFoundException::new);
    entity.setQuantity(entity.getQuantity() + valueChange);
    if (entity.getQuantity() < 1) {
      recipeItemRepository.deleteById(entity.getId());
    } else {
      recipeItemRepository.save(entity);
    }
  }

  private void addNewItemToRecipe(Recipe res, int itemId) {
    var item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    var entity = new RecipeItem(res, item, 1);
    recipeItemRepository.save(entity);
  }
}
