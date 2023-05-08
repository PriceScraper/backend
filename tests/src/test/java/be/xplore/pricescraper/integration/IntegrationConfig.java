package be.xplore.pricescraper.integration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("be.xplore.pricescraper")
@EntityScan("be.xplore.pricescraper")
@EnableJpaRepositories("be.xplore.pricescraper")
@TestConfiguration
@SpringBootConfiguration
public class IntegrationConfig {
}
