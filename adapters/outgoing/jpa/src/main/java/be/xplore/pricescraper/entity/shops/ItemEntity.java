package be.xplore.pricescraper.entity.shops;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 * Definition of an Item, not related to a {@link ShopEntity}.
 * The {@code specificItems} list contains {@link TrackedItemEntity},
 * that are linked to a specific {@link ShopEntity}
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Item")
@Getter
@Setter
@Indexed
public class ItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  @DocumentId
  private int id;
  @FullTextField(name = "itemname")
  private String name;
  private String image;
  private int quantity;
  private ItemUnitEntity unit;
  @Column(columnDefinition = "TEXT")
  private String ingredients;
  @OneToMany(fetch = FetchType.LAZY)
  private List<TrackedItemEntity> trackedItems;
}
