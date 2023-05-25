package be.xplore.pricescraper.controllers;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.dtos.CreateRecipeDto;
import be.xplore.pricescraper.dtos.ModifyRecipeItemDto;
import be.xplore.pricescraper.services.RecipeService;
import be.xplore.pricescraper.services.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for recipe actions.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/recipe")
public class RecipeController {
  private final RecipeService recipeService;
  private final UserService userService;

  /**
   * Create new recipe.
   */
  @PostMapping
  public ResponseEntity<Recipe> add(@RequestBody CreateRecipeDto dto,
                                    @AuthenticationPrincipal UserDetails userDetails) {
    var user = userService.loadUserByUsername(userDetails.getUsername());
    var res = recipeService.add(dto.title(), user);
    return ResponseEntity.ok(res);
  }

  /**
   * Get by filter.
   */
  @GetMapping
  public ResponseEntity<List<Recipe>> getByTitle(@RequestParam String filter) {
    var res = recipeService.getByFilter(filter);
    return ResponseEntity.ok(res);
  }

  /**
   * Get by creator.
   */
  @GetMapping("/personal")
  public ResponseEntity<List<Recipe>> getFromUser(
      @AuthenticationPrincipal UserDetails userDetails) {
    var user = userService.loadUserByUsername(userDetails.getUsername());
    var res = recipeService.getByCreator(user);
    return ResponseEntity.ok(res);
  }

  /**
   * Get by key.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Recipe> getById(@PathVariable("id") long id) {
    var res = recipeService.get(id).orElse(null);
    if (res == null) {
      return ResponseEntity.status(404).build();
    }
    return ResponseEntity.ok(res);
  }

  /**
   * Add item to recipe.
   */
  @PostMapping("/item/add")
  public ResponseEntity<Recipe> addItem(@RequestBody ModifyRecipeItemDto dto,
                                        @AuthenticationPrincipal UserDetails userDetails) {
    var user = userService.loadUserByUsername(userDetails.getUsername());
    var res = recipeService.addItemToRecipe(dto.recipeId(), dto.itemId(), user).orElse(null);
    if (res == null) {
      return ResponseEntity.status(404).build();
    }
    return ResponseEntity.ok(res);
  }

  /**
   * Remove item from recipe.
   */
  @PostMapping("/item/remove")
  public ResponseEntity<Recipe> removeItem(@RequestBody ModifyRecipeItemDto dto,
                                           @AuthenticationPrincipal UserDetails userDetails) {
    var user = userService.loadUserByUsername(userDetails.getUsername());
    var res = recipeService.removeItemFromRecipe(dto.recipeId(), dto.itemId(), user).orElse(null);
    if (res == null) {
      return ResponseEntity.status(404).build();
    }
    return ResponseEntity.ok(res);
  }
}
