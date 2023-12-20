package com.example.progettoSSD.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Order(1)
    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .headers(headers -> headers
                .xssProtection()
                    .xssProtectionEnabled(true)
                    .and()
                .referrerPolicy()
                    .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                    .and()
            )
            .authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/"), new AntPathRequestMatcher("/visualizzaHomework")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/riservata"), new AntPathRequestMatcher("/inserisciHomework"), new AntPathRequestMatcher("/cancellaHomework")).hasAuthority("ROLE_admin")
                .anyRequest().authenticated();

        http.oauth2Login()
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("https://localhost:8443/auth/realms/SSDProject/protocol/openid-connect/logout?redirect_uri=https://localhost:4433/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");
        http.exceptionHandling()
                .accessDeniedPage("/AccessDenied");
        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
        return authorities -> extracted(authorities);
    }

    private Collection<? extends GrantedAuthority> extracted(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
        var authority = authorities.iterator().next();
        boolean isOidc = authority instanceof OidcUserAuthority;
        if (isOidc) {
            var oidcUserAuthority = (OidcUserAuthority) authority;
            var userInfo = oidcUserAuthority.getUserInfo();

            if (userInfo.hasClaim("realm_access")) {
                var realmAccess = userInfo.getClaimAsMap("realm_access");
                final var roles = (Collection<String>) realmAccess.get("roles");
                mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
            }
        } else {
            var oauth2UserAuthority = (OAuth2UserAuthority) authority;
            Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

            if (userAttributes.containsKey("realm_access")) {
                var realmAccess = (Map<String, Object>) userAttributes.get("realm_access");
                final var roles = (Collection<String>) realmAccess.get("roles");
                mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
            }
        }
        return mappedAuthorities;
    }

    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Order(2)
    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .requestMatchers(new AntPathRequestMatcher("/"), new AntPathRequestMatcher("/visualizzaHomework")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/riservata"), new AntPathRequestMatcher("/inserisciHomework"), new AntPathRequestMatcher("/cancellaHomework")).hasAuthority("ROLE_admin")
            .anyRequest().authenticated();
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
}
