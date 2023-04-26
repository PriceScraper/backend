package be.xplore.pricescraper.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuring spring.
 */
@Configuration
@EnableScheduling
@ConfigurationPropertiesScan("be.xplore.pricescraper.config")
public class SpringConfig {
}
