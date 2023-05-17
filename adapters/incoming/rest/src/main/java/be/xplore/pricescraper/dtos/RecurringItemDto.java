package be.xplore.pricescraper.dtos;

import be.xplore.pricescraper.domain.shops.Item;

/**
 * RecurringItem without User.
 */
public record RecurringItemDto(long id, Item item, int quantity) {
}
