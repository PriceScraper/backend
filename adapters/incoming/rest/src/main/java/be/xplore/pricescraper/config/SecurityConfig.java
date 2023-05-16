package be.xplore.pricescraper.config;

import be.xplore.pricescraper.utils.security.JwtRequestFilter;
import be.xplore.pricescraper.utils.security.JwtSuccessHandler;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuring the behaviour of the web endpoints.
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
  private final JwtSuccessHandler jwtSuccessHandler;
  private final FrontendConfig frontendConfig;
  private final ClientRegistrationRepository clientRegistrationRepository;

  /**
   * Security configuration.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http.cors().and().csrf().ignoringRequestMatchers("/items/track")
        .ignoringRequestMatchers("/shoppinglists/**").ignoringRequestMatchers("/logout")
        .ignoringRequestMatchers("/auth/**").and().authorizeHttpRequests(
            auth -> auth.requestMatchers("/items/**").permitAll().requestMatchers("/items/track")
                .authenticated().anyRequest().authenticated()).oauth2Login(this::handleSuccess)
        .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .logout()
        //.logoutSuccessHandler(logoutSuccessHandler())
        .invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID")
        .logoutSuccessUrl(frontendConfig.getUrl() + "/logout").and().build();
  }

  /**
   * Add handlers.
   */
  private void handleSuccess(OAuth2LoginConfigurer<HttpSecurity> e) {
    e.successHandler(jwtSuccessHandler);
    e.authorizationEndpoint()
        .authorizationRequestResolver(authorizationRequestResolver(clientRegistrationRepository));
  }

  /**
   * To feed the SecurityContextHolder with the authentication details of the user.
   */
  @Bean
  public JwtRequestFilter tokenAuthenticationFilter() {
    return new JwtRequestFilter();
  }

  /**
   * To allow requests from the frontend.
   */
  @Bean
  public CorsFilter corsFilter() {
    var config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin(frontendConfig.getUrl());
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  /**
   * Resolver to customize oauth2 login request.
   */
  private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
      ClientRegistrationRepository clientRegistrationRepository) {

    var authorizationRequestResolver =
        new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
            "/oauth2/authorization");
    authorizationRequestResolver.setAuthorizationRequestCustomizer(
        authorizationRequestCustomizer());

    return authorizationRequestResolver;
  }

  /**
   * Adding params to auth request. Prompt=consent will enforce the login modal from GitHub.
   */
  private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
    return customizer -> customizer.additionalParameters(params -> params.put("prompt", "consent"));
  }
}
