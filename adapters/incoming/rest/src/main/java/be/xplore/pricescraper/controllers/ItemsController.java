package be.xplore.pricescraper.controllers;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemSearchResponseDto;
import be.xplore.pricescraper.dtos.TrackItem;
import be.xplore.pricescraper.services.ItemService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller for {@link be.xplore.pricescraper.domain.shops.Item}.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemsController {
  private final ItemService itemService;

  @GetMapping("/{id}")
  public Item findItemByIdWithLatestPrices(@PathVariable int id) {
    return itemService.findItemWithTrackedItemsAndLatestPricesById(id);
  }

  /**
   * Finds items by name that includes the given string.
   */
  @GetMapping
  public List<ItemSearchResponseDto> findItemsByNameLike(@RequestParam String name) {
    return itemService.findItemByNameLike(name).stream().map(itemSearchDto ->
        new ItemSearchResponseDto(itemSearchDto.getId(),
            itemSearchDto.getName(), itemSearchDto.getImage())).toList();
  }

  /**
   * Start tracking a specific item.
   */
  @PostMapping("/track")
  public TrackedItem trackItem(@RequestBody TrackItem trackItem) {
    var response = itemService.addTrackedItem(trackItem.url());
    return response;
  }
}
