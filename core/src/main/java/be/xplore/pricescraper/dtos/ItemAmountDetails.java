package be.xplore.pricescraper.dtos;

import be.xplore.pricescraper.domain.shops.UnitType;

/**
 * Data transfer object.
 */
public record ItemAmountDetails(UnitType type, double amount, int quantity) {
}
