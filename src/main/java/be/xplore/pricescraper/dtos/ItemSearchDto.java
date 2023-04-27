package be.xplore.pricescraper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto projection of a search of an {@link be.xplore.pricescraper.domain.shops.Item}.
 */
@Getter
@AllArgsConstructor
public class ItemSearchDto {
  private int id;
  private String name;
  private String image;
}
