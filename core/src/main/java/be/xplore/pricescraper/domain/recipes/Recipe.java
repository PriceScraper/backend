package be.xplore.pricescraper.domain.recipes;

import be.xplore.pricescraper.domain.users.User;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Combination of items.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
  private long id;
  private String title;
  private User creator;
  private List<RecipeItem> items;

  /**
   * No id and items list.
   */
  public Recipe(String title, User creator) {
    this.title = title;
    this.creator = creator;
    this.items = new ArrayList<>();
  }
}
