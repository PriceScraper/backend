package be.xplore.pricescraper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dto projection of a search of an {@link be.xplore.pricescraper.domain.shops.Item}.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchDto {
  private int id;
  private String name;
  private String image;
}
