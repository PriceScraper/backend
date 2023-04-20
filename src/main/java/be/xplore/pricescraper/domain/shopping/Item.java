package be.xplore.pricescraper.domain.shopping;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;

/**
 * A scraped item specific to a shop.
 */
@RequiredArgsConstructor
@Entity
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String description;
  @ManyToOne
  private Shop shop;
}
