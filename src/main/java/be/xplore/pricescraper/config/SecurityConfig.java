package be.xplore.pricescraper.config;

import be.xplore.pricescraper.util.JwtRequestFilter;
import be.xplore.pricescraper.util.JwtSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuring the behaviour of the web endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final JwtSuccessHandler jwtSuccessHandler;
  private final FrontendConfig frontendConfig;

  public SecurityConfig(JwtSuccessHandler jwtSuccessHandler, FrontendConfig frontendConfig) {
    this.jwtSuccessHandler = jwtSuccessHandler;
    this.frontendConfig = frontendConfig;
  }

  /**
   * Security configuration.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http
        .cors()
        .and()
        .csrf(c -> c
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        .authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated()
        )
        .oauth2Login(e -> e.successHandler(jwtSuccessHandler))
        .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
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
}
