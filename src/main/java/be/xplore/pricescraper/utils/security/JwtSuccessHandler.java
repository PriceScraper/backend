package be.xplore.pricescraper.utils.security;

import be.xplore.pricescraper.config.FrontendConfig;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Redirects to frontend whilst providing a JSON Web Token after successfully authenticating.
 */
@Slf4j
@Component
public class JwtSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements
    Serializable {
  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final FrontendConfig frontendConfig;
  @Serial
  private static final long serialVersionUID = -2550185165626007488L;

  /**
   * Constructor.
   */
  public JwtSuccessHandler(JwtProvider jwtProvider,
                           UserRepository userRepository, FrontendConfig frontendConfig) {
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
    this.frontendConfig = frontendConfig;
  }

  /**
   * Retrieves the authorities of the user, and converts it to a token to redirect.
   *
   * @param request        the request which caused the successful authentication
   * @param response       the response
   * @param authentication the <tt>Authentication</tt> object which was created during
   *                       the authentication process.
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException,
      ServletException {
    var userDetails =
        (OAuth2UserAuthority) authentication.getAuthorities().stream().toList().get(0);

    response.sendRedirect(frontendConfig.getUrl() + "/auth/" + getToken(userDetails));

    handle(request, response, authentication);

    clearAuthenticationAttributes(request);
  }

  /**
   * Create token from authority.
   */
  private String getToken(OAuth2UserAuthority userAuthority) {
    var user = getUser(userAuthority);
    return jwtProvider.generate(user.getId(), user.getUsername(),
        String.valueOf(userAuthority.getAttributes().get("avatar_url")));
  }

  /**
   * Retrieves the User details.
   */
  private User getUser(OAuth2UserAuthority userAuthority) {
    var user = userRepository.findByUsernameAndProvider(
        String.valueOf(userAuthority.getAttributes().get("login")), "github").orElse(null);
    if (user == null) {
      var tempuser = new User(String.valueOf(userAuthority.getAttributes().get("login")), "github");
      user = userRepository.save(tempuser);
      log.info("Registered new account for " + user.getUsername());
    }
    return user;
  }
}
