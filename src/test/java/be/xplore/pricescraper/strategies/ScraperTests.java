package be.xplore.pricescraper.strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ScraperTests {

    Scraper colruytScraper;

    @BeforeEach
    void setup() {
        colruytScraper = new ColruytScraper();
    }

    @Test
    void colruytReturnsPrice() {
        double price = colruytScraper.getPrice();
        assertTrue(price != 0);
    }

}
