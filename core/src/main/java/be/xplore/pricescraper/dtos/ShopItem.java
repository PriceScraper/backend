package be.xplore.pricescraper.dtos;

import java.util.Optional;

/**
 * Return object of the scraper.
 */
public record ShopItem(String title, double price, Optional<String> img) {
}
