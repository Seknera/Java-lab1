package ru.seknera.projects.telegramBot.config;

import ru.seknera.projects.telegramBot.model.UserAuthority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry
                                .requestMatchers(HttpMethod.GET, "/jokes").permitAll()
                                .requestMatchers(HttpMethod.GET, "/jokes/top5").permitAll()
                                .requestMatchers(HttpMethod.GET, "/jokes/random").permitAll()
                                .requestMatchers(HttpMethod.GET, "/jokes/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/jokes").hasAnyAuthority(UserAuthority.MODERATOR.getAuthority(), UserAuthority.USER.getAuthority())
                                .requestMatchers(HttpMethod.PUT, "/jokes/**").hasAnyAuthority(UserAuthority.MODERATOR.getAuthority())
                                .requestMatchers(HttpMethod.DELETE, "/jokes/**").hasAnyAuthority(UserAuthority.MODERATOR.getAuthority())
                                .requestMatchers("/registration", "/login").permitAll()
                                .anyRequest().hasAuthority(UserAuthority.ADMIN.getAuthority()))

                .formLogin(login -> {
                    try {
                        login.init(http);
                        login.loginProcessingUrl("/login");
                    } catch (Exception e) {
                        log.error("Error on init login {}", e.getMessage(), e);
                    }
                })
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}