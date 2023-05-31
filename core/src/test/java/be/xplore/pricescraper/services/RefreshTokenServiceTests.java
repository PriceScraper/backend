package be.xplore.pricescraper.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import be.xplore.pricescraper.domain.users.RefreshToken;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.UnauthorizedActionExeption;
import be.xplore.pricescraper.exceptions.UserNotFoundException;
import be.xplore.pricescraper.repositories.RefreshTokenRepository;
import be.xplore.pricescraper.repositories.UserRepository;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {RefreshTokenServiceImpl.class})
class RefreshTokenServiceTests {
  private final RefreshToken validToken = new RefreshToken();
  private final RefreshToken validToken2 = new RefreshToken();
  private final RefreshToken invalidToken = new RefreshToken();
  @MockBean
  private RefreshTokenRepository refreshTokenRepository;
  @MockBean
  private UserRepository userRepository;
  @Autowired
  private RefreshTokenService service;

  @BeforeEach
  void prepare() {
    var user = new User();
    user.setId(1L);
    when(userRepository.findById(1L))
        .thenReturn(Optional.of(user));
    validToken.setUser(user);
    validToken.setToken("valid-token");
    validToken.setExpiryDate(Instant.MAX);
    validToken2.setUser(user);
    validToken2.setToken("valid-token2");
    validToken2.setExpiryDate(Instant.MAX);
    invalidToken.setUser(user);
    invalidToken.setToken("invalid-token");
    invalidToken.setExpiryDate(Instant.MIN);
    when(refreshTokenRepository.findByToken(validToken.getToken()))
        .thenReturn(Optional.of(validToken));
    when(refreshTokenRepository.findByToken(invalidToken.getToken()))
        .thenReturn(Optional.of(validToken));
    when(refreshTokenRepository.save(any()))
        .thenReturn(validToken2);
  }

  @Test
  void refreshSuccess() {
    var res = service.refresh(validToken.getToken());
    assertNotNull(res);
    assertTrue(res.getToken().length() > 0);
    assertEquals(res.getToken(), validToken2.getToken());
    assertTrue(res.getExpiryDate().compareTo(Instant.now()) > 0);
  }

  @Test
  void refreshTokenNotFound() {
    assertThrows(UnauthorizedActionExeption.class, () -> {
      service.refresh("random");
    });
  }

  @Test
  void createRefreshTokenSuccess() {
    var res = service.createRefreshToken(1L);
    assertNotNull(res);
    assertTrue(res.getToken().length() > 0);
    assertEquals(res.getToken(), validToken2.getToken());
    assertTrue(res.getExpiryDate().compareTo(Instant.now()) > 0);
  }

  @Test
  void createRefreshTokenFailure() {
    assertThrows(UserNotFoundException.class, () -> {
      service.createRefreshToken(2L);
    });
  }
}
