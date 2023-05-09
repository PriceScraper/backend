package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.entity.users.ShoppingListEntity;
import be.xplore.pricescraper.repositories.ShoppingListRepository;
import be.xplore.pricescraper.repositories.jpa.ShoppingListJpaRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of JPA repository.
 */
@Repository
@AllArgsConstructor
public class ShoppingListRepositoryImpl implements ShoppingListRepository {
  private final ModelMapper modelMapper;
  private final ShoppingListJpaRepository jpaRepository;

  /**
   * Gets {@link ShoppingList} by id.
   */
  @Override
  public Optional<ShoppingList> getShoppingListById(int id) {
    return jpaRepository.getShoppingListById(id)
        .map(e -> modelMapper.map(e, ShoppingList.class));
  }

  /**
   * Saves a {@link ShoppingList}.
   */
  @Override
  public ShoppingList save(ShoppingList shoppingList) {
    var entity = modelMapper.map(shoppingList, ShoppingListEntity.class);
    entity = jpaRepository.save(entity);
    return modelMapper.map(entity, ShoppingList.class);
  }
}
