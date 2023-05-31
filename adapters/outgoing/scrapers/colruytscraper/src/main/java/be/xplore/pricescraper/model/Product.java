package be.xplore.pricescraper.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
  private String productId;
  private String technicalArticleNumber;
  private String commercialArticleNumber;
  private String name;
  private String brand;
  private String description;
  private String thumbNail;
  private String fullImage;
  private String content;
  private String LongName;
  private boolean isPriceAvailable;
  private Price price;
}
