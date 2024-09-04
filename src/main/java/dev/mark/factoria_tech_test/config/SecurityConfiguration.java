package dev.mark.factoria_tech_test.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import dev.mark.factoria_tech_test.users.security.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

	@Value("${api-endpoint}")
	String endpoint;

	@Autowired
    private AuthenticationEntryPoint CustomAuthenticationEntryPoint;

	JpaUserDetailsService jpaUserDetailsService;

	public SecurityConfiguration(JpaUserDetailsService jpaUserDetailsService) {
		this.jpaUserDetailsService = jpaUserDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.formLogin(form -> form.disable())
				.logout(out -> out
						.logoutUrl(endpoint + "/all/logout")
						.deleteCookies("JSESSIONID"))
				.authorizeHttpRequests(auth -> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers(endpoint + "/all/**").permitAll()
						.requestMatchers(endpoint + "/any/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers(endpoint + "/admin/**").hasRole("ADMIN")
						.requestMatchers(endpoint + "/user/**").hasRole("USER") 
						.requestMatchers(endpoint + "/imgs/**").permitAll()
						.anyRequest().authenticated())
				.userDetailsService(jpaUserDetailsService)
				.httpBasic(basic -> basic.authenticationEntryPoint(CustomAuthenticationEntryPoint))
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

		http.headers(header -> header.frameOptions(frame -> frame.sameOrigin()));

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}