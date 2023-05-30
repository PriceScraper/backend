package be.xplore.pricescraper.controllers;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.BarcodeScanResult;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.dtos.PotentialItemsCount;
import be.xplore.pricescraper.dtos.TrackItem;
import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import be.xplore.pricescraper.services.BarcodeItemService;
import be.xplore.pricescraper.services.ItemService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ItemsController {
  private final ItemService itemService;
  private final BarcodeItemService barcodeItemService;

  @GetMapping("/{id}")
  public Item findItemByIdWithLatestPrices(@PathVariable int id) {
    return itemService.findItemWithTrackedItemsAndLatestPricesById(id);
  }

  /**
   * Finds items by name that includes the given string.
   */
  @GetMapping
  public List<ItemSearchDto> findItemsByNameLike(@RequestParam String name) {
    return itemService.findItemByNameLike(name);
  }

  /**
   * Finds items by name that includes the given string.
   */
  @GetMapping("/potential")
  public ResponseEntity<PotentialItemsCount> getPotentialCount(@RequestParam String q) {
    var e = new PotentialItemsCount(itemService.getDiscoveredItems(q).size());
    return ResponseEntity.ok(e);
  }

  /**
   * Finds item name from barcode.
   */
  @GetMapping("/code")
  public ResponseEntity<BarcodeScanResult> findItemsByBarcode(@RequestParam String q) {
    try {
      var query = barcodeItemService.getProductName(q);
      return ResponseEntity.ok(new BarcodeScanResult(true, query));
    } catch (ItemNotFoundException e) {
      return ResponseEntity.ok(new BarcodeScanResult(false, e.getMessage()));
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Start tracking a specific item.
   */
  @PostMapping("/track")
  public TrackedItem trackItem(@RequestBody TrackItem trackItem) {
    return itemService.addTrackedItem(trackItem.url());
  }
}
