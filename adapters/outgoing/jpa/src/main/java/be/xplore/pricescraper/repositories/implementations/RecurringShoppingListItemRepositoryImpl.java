package be.xplore.pricescraper.repositories.implementations;

import be.xplore.pricescraper.domain.users.RecurringShoppingListItem;
import be.xplore.pricescraper.entity.users.RecurringShoppingListItemEntity;
import be.xplore.pricescraper.repositories.RecurringShoppingListItemRepository;
import be.xplore.pricescraper.repositories.jpa.RecurringShoppingListItemJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation for repository.
 */
@AllArgsConstructor
@Repository
public class RecurringShoppingListItemRepositoryImpl implements
    RecurringShoppingListItemRepository {
  private ModelMapper modelMapper;
  private RecurringShoppingListItemJpaRepository repository;

  @Override
  public void deleteById(long id) {
    repository.deleteById(id);
  }

  @Override
  public Optional<RecurringShoppingListItem> findById(long id) {
    return repository.findById(id).map(e -> modelMapper.map(e, RecurringShoppingListItem.class));
  }

  @Override
  public RecurringShoppingListItem save(RecurringShoppingListItem entity) {
    var mapped = modelMapper.map(entity, RecurringShoppingListItemEntity.class);
    var res = repository.save(mapped);
    return modelMapper.map(res, RecurringShoppingListItem.class);
  }

  @Override
  public List<RecurringShoppingListItem> findByUserId(long userId) {
    return repository.findByUser_Id(userId)
        .stream()
        .map(e -> modelMapper.map(e, RecurringShoppingListItem.class))
        .toList();
  }
}
