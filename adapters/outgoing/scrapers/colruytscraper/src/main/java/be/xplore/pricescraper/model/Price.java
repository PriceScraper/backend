package be.xplore.pricescraper.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Price {
  private float basicPrice;
  private String recommendedQuantity;
  private float measurementUnitPrice;
  private String measurementUnit;
  private boolean isRedPrice;
}
