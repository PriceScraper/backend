package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.recipes.RecipeItem;
import be.xplore.pricescraper.entity.recipe.RecipeItemEntity;
import be.xplore.pricescraper.repositories.RecipeItemRepository;
import be.xplore.pricescraper.repositories.jpa.RecipeItemJpaRepository;
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
public class RecipeItemRepositoryImpl implements RecipeItemRepository {
  private final ModelMapper modelMapper;
  private final RecipeItemJpaRepository repository;

  @Override
  public RecipeItem save(RecipeItem item) {
    var e = modelMapper.map(item, RecipeItemEntity.class);
    return modelMapper.map(repository.save(e), RecipeItem.class);
  }

  @Override
  public void deleteById(long id) {
    repository.deleteById(id);
  }

  @Override
  public List<RecipeItem> findItemsByRecipeId(long id) {
    return repository.findByRecipeId(id).stream()
        .map(e -> modelMapper.map(e, RecipeItem.class))
        .toList();
  }

  @Override
  public Optional<RecipeItem> findItemByRecipeIdAndItemId(long id, int itemId) {
    return repository.findByRecipeIdAndItemId(id, itemId)
        .map(e -> modelMapper.map(e, RecipeItem.class));
  }
}
