package be.xplore.pricescraper.scrapers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import be.xplore.pricescraper.scrapers.detail.LocalDummyScraper;
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
class LocalDummyScraperTests {

  private static final LocalDummyScraper scraper = new LocalDummyScraper();

  @Test
  void constructed() {
    assertNotNull(scraper);
  }

  @Test
  void shouldReturnItemResults() throws IOException {
    Resource resource = new ClassPathResource("html.txt");
    File file = resource.getFile();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String html = bufferedReader.lines().collect(Collectors.joining());
    Document doc = Jsoup.parse(html);
    MockJsoupConnection connection = new MockJsoupConnection(doc);

    try (MockedStatic<Jsoup> jsoup = Mockito.mockStatic(Jsoup.class)) {

      jsoup.when(() -> Jsoup.connect(any(String.class))).thenReturn(connection);

      var response = scraper.scrape("test");
      assertThat(response).isPresent();
      assertThat(response.get()).isNotNull();
      assertThat(response.get().title()).isNotNull();
      assertThat(response.get().price()).isPositive();
    }
  }

  @Test
  void shouldReturnEmptyResultsForIncorrectHtml() throws IOException {
    Resource resource = new ClassPathResource("html-incorrect.txt");
    File file = resource.getFile();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String html = bufferedReader.lines().collect(Collectors.joining());
    Document doc = Jsoup.parse(html);
    MockJsoupConnection connection = new MockJsoupConnection(doc);

    try (MockedStatic<Jsoup> jsoup = Mockito.mockStatic(Jsoup.class)) {

      jsoup.when(() -> Jsoup.connect(any(String.class))).thenReturn(connection);

      var response = scraper.scrape("test");
      assertThat(response).isEmpty();
    }
  }

}
