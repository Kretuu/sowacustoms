package pl.sowacustoms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.sowacustoms.security.authentication.oAuth2.HttpCookieOAuth2AuthorizationRequestRepository;
import pl.sowacustoms.security.authentication.oAuth2.OAuth2AuthenticationFailureHandler;
import pl.sowacustoms.security.authentication.oAuth2.OAuth2AuthenticationSuccessHandler;
import pl.sowacustoms.security.authentication.oAuth2.RestAuthenticationEntryPoint;
import pl.sowacustoms.security.authentication.services.CustomOAuth2UserService;
import pl.sowacustoms.security.authentication.services.CustomUserDetailsService;
import pl.sowacustoms.security.authorization.TokenAuthenticationFilter;
import pl.sowacustoms.user.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigurer {
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    private UserService userService;


//    @Bean
//    protected UserDetailsService customUserDetailsService() {
//        return new CustomUserDetailsService();
//    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       CustomUserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and().build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and().authorizeHttpRequests()
//                .requestMatchers("/api").hasAuthority("ROLE_USER")
                .requestMatchers("/**",
                        "/error",
                        "/favicon.ico",
                        "/*/*/*.png",
                        "/*/*.js",
                        "/*/*.css",
                        "/*/*.html").permitAll()
                .requestMatchers("/login", "/refresh", "/auth/**", "/oauth2/**", "/api/**").permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login().authorizationEndpoint().baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and().redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and().successHandler(oAuth2AuthenticationSuccessHandler).failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


//    @Bean
//    public static PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
