package be.xplore.pricescraper;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemUnit;
import be.xplore.pricescraper.matchers.ImageRecognitionMatcher;
import be.xplore.pricescraper.matchers.ItemMatcherFactoryImpl;
import jakarta.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ItemMatcherFactoryImpl.class)
public class ImageRecognitionMatcherIT {

  @Autowired
  ItemMatcherFactoryImpl factory;


  private final static Item itemA;

  static {
    try {
      String base64 = DatatypeConverter.printBase64Binary(
          Objects.requireNonNull(
                  ImageRecognitionMatcherIT.class.getResourceAsStream("/pizza1.jfif"))
              .readAllBytes());
      itemA = new Item("a",
          "data:image/jpeg;base64," + base64,
          1,
          new ItemUnit(ItemUnit.UnitType.KILOGRAMS, 1), "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final static Item itemB;

  static {
    try {
      String base64 = DatatypeConverter.printBase64Binary(
          Objects.requireNonNull(
                  ImageRecognitionMatcherIT.class.getResourceAsStream("/pizza2.png"))
              .readAllBytes());
      itemB = new Item("b",
          "data:image/png;base64," + base64,
          1,
          new ItemUnit(ItemUnit.UnitType.KILOGRAMS, 1), "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void shouldMatch() {
    ImageRecognitionMatcher imageRecognitionMatcher =
        (ImageRecognitionMatcher) factory.makeByNameAndItems("imagerecognition", itemA, itemB);
    assertThat(imageRecognitionMatcher.isMatching()).isTrue();
  }

}
