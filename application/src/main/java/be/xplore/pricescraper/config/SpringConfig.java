package be.xplore.pricescraper.config;

import be.xplore.pricescraper.factories.ItemMatcherCombinerFactory;
import be.xplore.pricescraper.utils.matchers.Combiner;
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
    return matcherCombinerFactory.makeWeightedCombiner();
  }

}
