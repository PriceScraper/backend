package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.entity.recipe.RecipeEntity;
import be.xplore.pricescraper.repositories.RecipeRepository;
import be.xplore.pricescraper.repositories.jpa.RecipeJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of repository.
 */
@Repository
@AllArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepository {
  private ModelMapper modelMapper;
  private RecipeJpaRepository repository;

  /**
   * Save entity.
   */
  @Override
  public Recipe save(Recipe recipe) {
    var entity = modelMapper.map(recipe, RecipeEntity.class);
    var saved = repository.saveAndFlush(entity);
    return modelMapper.map(saved, Recipe.class);
  }

  /**
   * Find by creator id.
   */
  @Override
  public List<Recipe> findByCreatorId(long userId) {
    var res = repository.findByCreatorId(userId);
    return res.stream()
        .map(e -> modelMapper.map(e, Recipe.class))
        .toList();
  }

  /**
   * Find by pk.
   */
  @Override
  public Optional<Recipe> findById(long id) {
    return repository.findById(id).map(e -> modelMapper.map(e, Recipe.class));
  }

  /**
   * Find by filter.
   */
  @Override
  public List<Recipe> findByTitle(String filter) {
    return repository.findByTitleContainsIgnoreCase(filter).stream()
        .map(e -> modelMapper.map(e, Recipe.class))
        .toList();
  }

  @Override
  public long count() {
    return repository.count();
  }
}
