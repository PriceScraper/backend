package be.xplore.pricescraper.seed;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.ShopRepository;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import org.springframework.stereotype.Component;

/**
 * Seed of items.
 */
@Component
public class ItemSeed {
  private final ItemRepository itemRepository;
  private final TrackedItemRepository trackedItemRepository;
  private final ShopRepository shopRepository;

  /**
   * Starting item seed.
   */
  public ItemSeed(ItemRepository itemRepository, TrackedItemRepository trackedItemRepository,
                  ShopRepository shopRepository) {
    this.itemRepository = itemRepository;
    this.trackedItemRepository = trackedItemRepository;
    this.shopRepository = shopRepository;
    this.seed();
  }

  /**
   * Executing seed.
   */
  private void seed() {

    var s1 = new Shop();
    s1.setName("Carrefour");
    s1.setUrl("carrefour.be");
    s1 = shopRepository.save(s1);

    var i1 = itemRepository.save(new Item("Blond Bier Krat 24 x 25 cl",
        "https://cdn.carrefour.eu/300_00165431_5410228134527_00.webp", 1, null, null));
    trackedItemRepository.save(new TrackedItem(
        "Dranken/Bier/Pils/In-krat/Jupiler%7CBlond-Bier-Krat-24-x-25-cl/p/00165431",
        s1, i1));
    trackedItemRepository.save(new TrackedItem(
        "wi222810/jupiler-pils-bak-24x25cl-bel",
        s1, i1));

    var i2 = itemRepository.save(new Item("Extra Gemalen Koffie Espresso 250 g",
        "https://cdn.carrefour.eu/300_04002099_5400101164864_00.webp", 1, null, ""));
    trackedItemRepository.save(new TrackedItem(
        "Kruidenierswaren/Ontbijt/Koffie/Gemalen-koffie"
            + "/Carrefour%7CExtra-Gemalen-Koffie-Espresso-250-g/p/04002099",
        s1, i2));

    var s2 = new Shop();
    s2.setName("Albert Heijn");
    s2.setUrl("ah.be");
    s2 = shopRepository.save(s2);

    var s3 = new Shop();
    s3.setName("Aldi");
    s3.setUrl("aldi.be");
    s3 = shopRepository.save(s3);

    var i3 = itemRepository.save(new Item("Water 2L",
        "https://cdn.carrefour.eu/300_04412972_5410013109747_00.webp",
        1, null, ""));
    trackedItemRepository.save(new TrackedItem(
        "Dranken/Water/Niet-bruisend-water"
            + "/Spa%7CREINE-Niet-Bruisend-Natuurlijk-Mineraalwater-8x1-5L/p/04412972",
        s1, i3));
    trackedItemRepository.save(new TrackedItem(
        "wi550058/bar-le-duc-mineraalwater-pak",
        s2, i3));
    trackedItemRepository.save(new TrackedItem(
        "bronwater-320-1-0.article.html",
        s3, i3));

  }
}
