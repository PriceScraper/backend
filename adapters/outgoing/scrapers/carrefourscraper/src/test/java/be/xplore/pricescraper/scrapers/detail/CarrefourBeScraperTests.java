package be.xplore.pricescraper.scrapers.detail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import be.xplore.pricescraper.scrapers.MockJsoupConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class CarrefourBeScraperTests {

  private static final CarrefourBeScraper scraper = new CarrefourBeScraper();

  @Test
  void constructed() {
    assertNotNull(scraper);
  }

  @Test
  void getItemResults() throws IOException {
    Resource resource = new ClassPathResource("html.txt");
    File file = resource.getFile();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String html = bufferedReader.lines().collect(Collectors.joining());
    Document doc = Jsoup.parse(html);
    MockJsoupConnection connection = new MockJsoupConnection(doc);

    try (MockedStatic<Jsoup> jsoup = Mockito.mockStatic(Jsoup.class)) {

      jsoup.when(() -> Jsoup.connect(any(String.class))).thenReturn(connection);

      var response = scraper.scrape("test");
      assertTrue(response.isPresent());
      assertNotNull(response.get());
      assertNotNull(response.get().title());
      assertThat(response.get().nutritionValues().get()).containsKey("Energie");
      assertThat(response.get().nutritionValues().get().get("Energie")).endsWith("kcal");
      assertTrue(response.get().price() > 0);
    }
  }
}
