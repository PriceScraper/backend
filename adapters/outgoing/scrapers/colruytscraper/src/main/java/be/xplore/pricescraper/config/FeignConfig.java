package be.xplore.pricescraper.config;

import be.xplore.pricescraper.utils.ColruytHttpClient;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {ColruytHttpClient.class})
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class FeignConfig {
}
