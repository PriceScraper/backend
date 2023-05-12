package be.xplore.pricescraper.controllers;

import be.xplore.pricescraper.dtos.NewTokenDto;
import be.xplore.pricescraper.dtos.RefreshAndAccessTokenDto;
import be.xplore.pricescraper.services.RefreshTokenService;
import be.xplore.pricescraper.utils.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle authorization requests.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
  private RefreshTokenService refreshTokenService;
  private JwtProvider jwtProvider;

  /**
   * To get a new access token and invalidate the previous refresh token.
   */
  @PostMapping("/refresh")
  public ResponseEntity<RefreshAndAccessTokenDto> refresh(@RequestBody NewTokenDto dto) {
    var refreshToken = refreshTokenService.refresh(dto.refreshToken());
    log.info("Refresh: " + refreshToken);
    var accessToken = jwtProvider.generate(refreshToken.getUser());
    log.info("Access: " + refreshToken);
    log.info(
        "Refreshed token for user "
            + refreshToken.getUser().getId()
            + ", new access token: "
            + accessToken);
    return ResponseEntity.ok(new RefreshAndAccessTokenDto(refreshToken.getToken(), accessToken));
  }

}
