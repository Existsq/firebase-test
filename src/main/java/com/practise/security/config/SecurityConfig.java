package com.practise.security.config;

import com.practise.security.filter.JsonUsernamePasswordAuthFilter;
import com.practise.security.filter.JwtAuthenticationFilter;
import com.practise.security.handler.RestAccessDeniedHandler;
import com.practise.security.handler.RestAuthenticationEntryPoint;
import com.practise.security.handler.RestAuthenticationFailureHandler;
import com.practise.security.handler.RestAuthenticationSuccessHandler;
import com.practise.security.provider.JwtAuthenticationProvider;
import com.practise.security.service.jwt.JwtService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
  private final RestAccessDeniedHandler restAccessDeniedHandler;
  private final RestAuthenticationFailureHandler authenticationFailureHandler;
  private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  /** Основной SecurityFilterChain */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationManager authManager = authenticationManager();

    http.csrf(AbstractHttpConfigurer::disable)
        .requestCache(RequestCacheConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/login", "/register")
                    .permitAll()
                    .requestMatchers("/secured")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(
            jwtAuthenticationFilter(authManager), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(
            jsonUsernamePasswordAuthFilter(
                authManager, restAuthenticationSuccessHandler, authenticationFailureHandler),
            JwtAuthenticationFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(restAuthenticationEntryPoint)
                    .accessDeniedHandler(restAccessDeniedHandler));
    return http.build();
  }

  /** Создаем AuthenticationManager с двумя провайдерами */
  @Bean
  public AuthenticationManager authenticationManager() {
    List<AuthenticationProvider> providers =
        List.of(daoAuthenticationProvider(), jwtAuthenticationProvider());
    return new ProviderManager(providers);
  }

  /** Провайдер для логина по имени/паролю из базы */
  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  /** Кастомный фильтр для валидации JWT в запросах */
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(
      AuthenticationManager authenticationManager) {
    return new JwtAuthenticationFilter(authenticationManager);
  }

  /** Провайдер для аутентификации по JWT */
  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider() {
    return new JwtAuthenticationProvider(jwtService);
  }

  /** Кастомный фильтр для обработки JSON логина */
  @Bean
  public JsonUsernamePasswordAuthFilter jsonUsernamePasswordAuthFilter(
      AuthenticationManager authenticationManager,
      RestAuthenticationSuccessHandler successHandler,
      RestAuthenticationFailureHandler failureHandler) {
    JsonUsernamePasswordAuthFilter filter = new JsonUsernamePasswordAuthFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationSuccessHandler(successHandler);
    filter.setAuthenticationFailureHandler(failureHandler);
    return filter;
  }

  /** Парольный энкодер */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
