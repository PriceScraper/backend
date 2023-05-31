package be.xplore.pricescraper.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Price {
  private double basicPrice;
  private String recommendedQuantity;
  private double measurementUnitPrice;
  private String measurementUnit;
  private boolean isRedPrice;
}
