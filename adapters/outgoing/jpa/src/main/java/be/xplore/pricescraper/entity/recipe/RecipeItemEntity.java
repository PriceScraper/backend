package be.xplore.pricescraper.entity.recipe;

import be.xplore.pricescraper.entity.shops.ItemEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity.
 */
@Entity(name = "RecipeItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private long id;
  @ManyToOne
  @JoinColumn(name = "recipe_id")
  private RecipeEntity recipe;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "item_id")
  private ItemEntity item;
  private int quantity;
}
