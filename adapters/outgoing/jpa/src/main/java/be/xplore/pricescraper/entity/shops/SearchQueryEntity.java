package be.xplore.pricescraper.entity.shops;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An enqueued request to find items matching the search query.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SearchQuery")
@Getter
@Setter
public class SearchQueryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;
  private String query;
  private LocalDateTime dateTime;
}
