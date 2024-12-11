package com.zam.security.security;

import com.zam.security.security.clients.ClientRegistrationConfig;
import com.zam.security.security.requests.CustomAccessDeniedHandler;
import com.zam.security.security.requests.CustomAuthEntryPoint;
import com.zam.security.security.filters.JwtCookieTokenFilter;
import com.zam.security.security.requests.CustomAuthenticationFailureHandler;
import com.zam.security.security.requests.CustomAuthenticationSuccessHandler;
import com.zam.security.security.services.CustomOidcUserService;
import com.zam.security.security.services.UserDetailsServiceImpl;
import com.zam.security.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final ClientRegistrationConfig clientRegistrationConfig;
    private final CustomOidcUserService customOidcUserService;

    public WebSecurityConfig(JwtUtil jwtUtil, CustomAuthEntryPoint customAuthEntryPoint,
                             CustomAccessDeniedHandler customAccessDeniedHandler,
                             ClientRegistrationConfig clientRegistrationConfig,
                             CustomOidcUserService customOidcUserService) {
        this.jwtUtil = jwtUtil;
        this.customAuthEntryPoint = customAuthEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.clientRegistrationConfig = clientRegistrationConfig;
        this.customOidcUserService = customOidcUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(c -> c.configurationSource(configurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManConfig -> sessionManConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            //authorizeHttp.requestMatchers("/api/maintenance/**").permitAll();
                            authorizeHttp.requestMatchers("/api/auth/signIn").permitAll();
                            authorizeHttp.requestMatchers("/api/auth/signUp").permitAll();
                            authorizeHttp.requestMatchers("/api/auth/validate").permitAll();
                            authorizeHttp.requestMatchers(HttpMethod.GET, "/api/catalog/**").permitAll();
                            authorizeHttp.requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll();
                            authorizeHttp.requestMatchers("/api/maintenance/**").hasAuthority("ROLE_ADMIN");
                            authorizeHttp.anyRequest().authenticated();
                        }
                )
                .oauth2Login(oauth -> oauth
                        .clientRegistrationRepository(clientRegistrationConfig.clientRegistrationRepository())
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
                        .successHandler(new CustomAuthenticationSuccessHandler(jwtUtil))
                        .failureHandler(new CustomAuthenticationFailureHandler())
                )
                .addFilterBefore(new JwtCookieTokenFilter(jwtUtil), AuthorizationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
