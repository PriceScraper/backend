package be.xplore.pricescraper.config;

import be.xplore.pricescraper.factories.ItemMatcherCombinerFactory;
import be.xplore.pricescraper.matchers.Combiner;
import be.xplore.pricescraper.matchers.IngredientMatcher;
import be.xplore.pricescraper.matchers.Matcher;
import be.xplore.pricescraper.matchers.NutritionValueMatcher;
import be.xplore.pricescraper.matchers.TitleMatcher;
import be.xplore.pricescraper.matchers.UnitMatcher;
import java.util.HashMap;
import java.util.Map;
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
  @Bean
  Combiner getCombiner(ItemMatcherCombinerFactory matcherCombinerFactory) {
    Map<Matcher, Double> matchersWithWeights = new HashMap<>();
    matchersWithWeights.put(new NutritionValueMatcher(), 0.5);
    matchersWithWeights.put(new IngredientMatcher(), 0.15);
    matchersWithWeights.put(new UnitMatcher(), 0.3);
    matchersWithWeights.put(new TitleMatcher(), 0.05);
    return matcherCombinerFactory.makeWeightedCombiner(matchersWithWeights);
  }

}
