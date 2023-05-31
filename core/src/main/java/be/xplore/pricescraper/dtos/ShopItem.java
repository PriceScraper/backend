package be.xplore.pricescraper.dtos;

import java.util.Map;
import java.util.Optional;

/**
 * Return object of the scraper.
 */
public record ShopItem(String title, double price, Optional<String> img,
                       Optional<ItemAmountDetails> details,
                       Optional<String> ingredients,
                       Optional<Map<String, String>> nutritionValues) {
}
