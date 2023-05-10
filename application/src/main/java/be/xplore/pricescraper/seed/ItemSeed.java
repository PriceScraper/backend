package be.xplore.pricescraper.seed;

import be.xplore.pricescraper.services.ItemService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Seed of items.
 */
@Profile("!test")
@AllArgsConstructor
@Slf4j
@Component
public class ItemSeed {
  private final ItemService itemService;

  /**
   * Executing seed.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void seedItems() {
    if (itemService.trackedItemsCount() > 0) {
      log.info("Not seeding because db is not empty.");
      return;
    }
    log.info("Seeding items.");
    var items = new String[] {
        "https://www.ah.be/producten/product/wi500306/duvel-fles",
        "https://www.ah.be/producten/product/wi505909/tripel-4-pack",
        "https://www.ah.be/producten/product/wi177810/blond-4-pack",
        "https://www.ah.be/producten/product/wi158733/weizener-6-pack",
        "https://drive.carrefour.be/nl/Dranken/Warme-dranken/Koffie/Senseo-compatibel-Pads/Senseo%7CKoffie-Pads-Cappuccino-8-Stuks/p/05534371",
        "https://drive.carrefour.be/nl/Baby/Babyvoeding/Snacks-%26-desserts/Desserts-met-fruit/Vanaf-6-maand/Carrefour-Baby%7CBio-Appel%2C-Aardbei-vanaf-6-Maanden-4-x-100-g/p/06358717",
        "https://drive.carrefour.be/nl/Dranken/Water/Niet-bruisend-water/Carrefour%7CNatuurlijk-Mineraalwater-Uit-de-Alpen-6-x-50-cl/p/05340905",
        "https://drive.carrefour.be/nl/Kruidenierswaren/Koeken-%26-taarten/Koeken/Ontbijtkoeken/LU%7CBelVita-Ontbijtkoeken-Multi-granen-300-g/p/04406559",
        "https://drive.carrefour.be/nl/Dranken/Softdrinks/Sport--%26-gezonde-dranken/Aquarius%7CRed-Peach-6-x-500-ml/p/06093624",
        "https://www.aldi.be/nl/p/jupiler-15-st-3001592-1-0.article.html",
        "https://www.aldi.be/nl/p/bier-met-tequilasmaak-772-1-0.article.html",
        "https://www.aldi.be/nl/p/bananen-8272-1-0.article.html",
        "https://www.lidl.be/p/nl-BE/bleekwater/p740192819",
        "https://www.lidl.be/p/nl-BE/brita-waterfilterfles/p100358397",
        "https://www.lidl.be/p/nl-BE/silvercrest-handstofzuiger/p100346327"
    };

    Arrays.stream(items).toList()
        .forEach(i -> {
          log.debug("Seeding item " + i);
          itemService.addTrackedItem(i);
        });
  }
}
