package be.xplore.price_scraper.factories;

import be.xplore.price_scraper.exceptions.ScraperCreateException;
import be.xplore.price_scraper.strategies.ColruytScraper;
import be.xplore.price_scraper.strategies.Scraper;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ScraperFactory {
    private HashMap<String, Scraper> implementations;

    public Scraper createScraper(String name) {
        Scraper scraper = this.implementations.get(name.toLowerCase());
        if(scraper == null) throw new ScraperCreateException("Could not create scraper from factory, is the provided name correct");
        return scraper;
    }

    public ScraperFactory() {
        this.implementations = new HashMap<>();
        this.implementations.put("colruyt", new ColruytScraper());
    }
}
