package be.xplore.pricescraper.util;

import be.xplore.pricescraper.exceptions.UserNotFoundException;
import be.xplore.pricescraper.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Feeds the SecurityContextHolder with the principals of the user.
 */
public class JwtRequestFilter extends OncePerRequestFilter {
  @Autowired
  private JwtProvider jwtProvider;
  @Autowired
  private UserRepository userRepository;

  /**
   * Executes the filter to feed the SecurityContextHolder.
   */
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = getJwtFromRequest(request);

      if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
        var userId = jwtProvider.getUserIdFromToken(jwt);
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        setSecurityContext(request, user);
      }
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }

    removeCookie(response);

    filterChain.doFilter(request, response);
  }

  /**
   * Set SecurityContext in SecurityContextHolder.
   */
  private void setSecurityContext(HttpServletRequest request, UserDetails userDetails) {
    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /**
   * Removes the cookie from the response.
   */
  private void removeCookie(HttpServletResponse response) {
    var overwriteCookie = new Cookie("JSESSIONID", null);
    overwriteCookie.setMaxAge(0);
    response.addCookie(overwriteCookie);
  }

  /**
   * Retrieves the JWT token from the headers.
   */
  private String getJwtFromRequest(HttpServletRequest request) {
    return request.getHeader("Authorization");
  }
}
