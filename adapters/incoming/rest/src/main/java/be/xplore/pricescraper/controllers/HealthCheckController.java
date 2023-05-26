package be.xplore.pricescraper.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Healthcheck endpoint for deployment.
 */
@RestController
@RequestMapping("/healthcheck")
public class HealthCheckController {

  @GetMapping
  public ResponseEntity<String> checkHealth() {
    return ResponseEntity.ok("Healthcheck succeeded");
  }

}
