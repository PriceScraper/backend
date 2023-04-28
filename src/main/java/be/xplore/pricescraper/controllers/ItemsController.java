package be.xplore.pricescraper.controllers;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.dtos.TrackItem;
import be.xplore.pricescraper.services.ItemService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

  @GetMapping
  public List<ItemSearchDto> findItemsByNameLike(@RequestParam String name) {
    return itemService.findItemByNameLike(name);
  }

  /**
   * Start tracking a specific item.
   */
  @PostMapping("/track")
  public ResponseEntity<Object> trackItem(@RequestBody TrackItem trackItem) {
    var response = itemService.addTrackedItem(trackItem.url());
    if (!response.hasSucceeded()) {
      return ResponseEntity
          .badRequest()
          .body(response.message());
    }
    return ResponseEntity.ok(response.object());
  }
}
