package be.xplore.pricescraper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.repositories.RecipeItemRepository;
import be.xplore.pricescraper.repositories.RecipeRepository;
import be.xplore.pricescraper.scrapers.ItemScraper;
import be.xplore.pricescraper.scrapers.detail.CarrefourBeScraper;
import be.xplore.pricescraper.services.ItemServiceImpl;
import be.xplore.pricescraper.services.RecipeService;
import be.xplore.pricescraper.services.ScraperServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@Import(IntegrationConfig.class)
@SpringBootTest(classes = {ItemServiceImpl.class, ItemScraper.class,
    ScraperServiceImpl.class, CarrefourBeScraper.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ActiveProfiles("test")
@Transactional
class RecipeServiceIT {
  @Autowired
  private RecipeService recipeService;
  @Autowired
  private RecipeRepository recipeRepository;
  @Autowired
  private RecipeItemRepository recipeItemRepository;

  @Test
  void add() {
    var countBefore = recipeRepository.count();
    var addResponse = recipeService.add("testCase", null);
    assertNotNull(addResponse);
    assertTrue(addResponse.getId() > 0);
    var countAfter = recipeRepository.count();
    assertEquals(countBefore + 1, countAfter);

    var getResponse = recipeService.get(addResponse.getId());
    assertTrue(getResponse.isPresent());
    assertEquals(addResponse.getId(), getResponse.get().getId());
    assertEquals(addResponse.getTitle(), getResponse.get().getTitle());
    assertEquals(addResponse.getCreator(), getResponse.get().getCreator());
    assertEquals(addResponse.getItems(), getResponse.get().getItems());
  }

  @Test
  void get() {
    var addResponse = recipeService.add("testCase", null);
    assertNotNull(addResponse);

    var getResponse = recipeService.get(addResponse.getId());
    assertTrue(getResponse.isPresent());
    assertEquals(addResponse.getId(), getResponse.get().getId());
    assertEquals(addResponse.getTitle(), getResponse.get().getTitle());
    assertEquals(addResponse.getCreator(), getResponse.get().getCreator());
    assertEquals(addResponse.getItems(), getResponse.get().getItems());
  }

  @Test
  void getByFilterSuccess() {
    var title = "TesTCasE";
    var searchQuery = title.toLowerCase();

    assertNotEquals(title, searchQuery);
    assertEquals(title.toLowerCase(), searchQuery.toLowerCase());

    var countBefore = recipeService.getByFilter(searchQuery).size();
    var addResponse = recipeService.add(title, null);
    assertNotNull(addResponse);
    var countAfter = recipeService.getByFilter(searchQuery).size();
    assertEquals(countBefore + 1, countAfter);
  }

  @Test
  void getByFilterFailure() {
    var title = "TesTCasE";
    var searchQuery = new StringBuilder(title).reverse().toString();

    assertNotEquals(title.toLowerCase(), searchQuery.toLowerCase());

    var countBefore = recipeService.getByFilter(searchQuery).size();
    var addResponse = recipeService.add(title, null);
    assertNotNull(addResponse);
    var countAfter = recipeService.getByFilter(searchQuery).size();
    assertEquals(countBefore, countAfter);
  }
}
