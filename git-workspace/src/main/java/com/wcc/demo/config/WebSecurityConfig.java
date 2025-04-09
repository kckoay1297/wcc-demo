package com.wcc.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/postcode/updatePostcode").hasRole("ADMIN")
                .requestMatchers("/api/postcode/deleteAllPostcode").hasRole("ADMIN")
                .requestMatchers("/api/postcode/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/distance/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
			)
			.httpBasic()
			.and()
            .logout(logout -> logout
                .permitAll()
            )
			.csrf(csrf -> csrf.disable());


        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return user -> {
            switch (user) {
                case "admin":
                    return User.withUsername("admin")
                               .password(passwordEncoder().encode("admin123"))
                               .roles("ADMIN")
                               .build();
                case "user":
                    return User.withUsername("user")
                               .password(passwordEncoder().encode("user123"))
                               .roles("USER")
                               .build();
                default:
                    throw new IllegalArgumentException("Unknown user");
            }
        };
    }
    
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//		.csrf(csrf -> csrf.disable())
//			.authorizeHttpRequests((requests) -> requests
//				.requestMatchers("/", "/api/**").permitAll()
//				.requestMatchers(HttpMethod.POST, "/*").permitAll()
//				//.anyRequest().authenticated()
//			)
//			.csrf(csrf -> csrf.disable());
//
//
//		return http.build();
//	}
	
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()
//            .authorizeHttpRequests()
//                .anyRequest().authenticated()
//                .and()
//            .httpBasic();
//        return http.build();
//    }

//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build();
//        return new InMemoryUserDetailsManager(user);
//    }
}
