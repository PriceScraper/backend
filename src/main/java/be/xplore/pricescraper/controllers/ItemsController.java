package be.xplore.pricescraper.controllers;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import be.xplore.pricescraper.services.ItemService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller for {@link be.xplore.pricescraper.domain.shops.Item}.
 */
@RestController
@RequestMapping(value = "items", produces = "application/json")
@CrossOrigin("http://localhost:3000")
public class ItemsController {

  private ItemService itemService;

  public ItemsController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/{id}")
  public Item findItemByIdWithLatestPrices(@PathVariable int id) {
    return itemService.findItemWithTrackedItemsAndLatestPricesById(id);
  }

  @GetMapping("")
  public List<ItemSearchDto> findItemsByNameLike(@RequestParam String name) {
    return itemService.findItemByNameLike(name);
  }

}
