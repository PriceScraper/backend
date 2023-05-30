package be.xplore.pricescraper.config;

import be.xplore.pricescraper.factories.ItemMatcherCombinerFactory;
import be.xplore.pricescraper.matchers.Combiner;
import be.xplore.pricescraper.matchers.IngredientMatcher;
import be.xplore.pricescraper.matchers.Matcher;
import be.xplore.pricescraper.matchers.TitleMatcher;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuring spring.
 */
@Configuration
@EnableScheduling
@ConfigurationPropertiesScan("be.xplore.pricescraper.config")
public class SpringConfig {

  @Autowired
  ItemMatcherCombinerFactory matcherCombinerFactory;

  @Bean
  Combiner getCombiner() {
    Map<Matcher, Double> matchersWithWeights = new HashMap<>();
    matchersWithWeights.put(new IngredientMatcher(), 0.6);
    matchersWithWeights.put(new TitleMatcher(), 0.4);
    return matcherCombinerFactory.makeWeightedCombiner(matchersWithWeights);
  }

}
