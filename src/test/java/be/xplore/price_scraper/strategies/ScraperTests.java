package be.xplore.price_scraper.strategies;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScraperTests {

    Scraper colruytScraper;

    @BeforeAll
    void setup() {
        colruytScraper = new ColruytScraper();
    }

    @Test
    void colruytReturnsPrice() {
        double price = colruytScraper.getPrice();
        assertTrue(price != 0);
    }

}