
package com.DionysOS.Eatmoji.config;

import com.DionysOS.Eatmoji.service.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CorsConfigurationSource corsConfigSource;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigSource) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.corsConfigSource = corsConfigSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .requiresChannel(channel -> channel.anyRequest().requiresSecure()) // HTTPS 강제 적용
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigSource))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll() // Root 엔드포인트 허용
                        .requestMatchers("/auth/**").permitAll() // 인증 관련 엔드포인트 전면 허용
                        .requestMatchers("/api/recommend/emoji").permitAll() // 이모지 추천 API (로그인 없이 하는 것) 전면 허용
                        .requestMatchers("/api/recipe/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated() // 나머지는 로그인 전용
                )
                .formLogin(form -> form.disable())
                .securityContext(security -> security.requireExplicitSave(false))
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}