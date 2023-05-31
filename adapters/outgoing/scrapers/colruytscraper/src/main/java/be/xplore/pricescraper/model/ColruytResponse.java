package be.xplore.pricescraper.model;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColruytResponse {
  private String source;
  private float productsFound;
  private float productsReturned;
  private float productsAvailable;
  private ArrayList<SmallProduct> products = new ArrayList<>();
}
