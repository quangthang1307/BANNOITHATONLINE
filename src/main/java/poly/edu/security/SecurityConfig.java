package poly.edu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import poly.edu.oauth2.CustomerOauth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomAccountDetailService customAccountDetailService;

	@Autowired
	private CustomerOauth2UserService oAuth2UserService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((auth) -> auth
						.requestMatchers("/*", "/user/**", "/rest/**").permitAll()
						.requestMatchers("/admin/**", "/employee/**").hasAuthority("ADMIN")
						.requestMatchers("/employee/**").hasAuthority("EMPLOYEE")
						.anyRequest().authenticated())
				.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/login")
						.usernameParameter("username").passwordParameter("password")
						.defaultSuccessUrl("/default", true)
						.successHandler(new CustomAuthenticationSuccessHandler())
						.failureUrl("/login?error=true"))
						
				// .oauth2Login().loginPage("/login").userInfoEndpoint().userService(oAuth2UserService);

				.oauth2Login(oauth2 -> oauth2
						.loginPage("/login")
						.userInfoEndpoint().userService(oAuth2UserService)
						.and()
						.successHandler(new CustomAuthenticationSuccessHandler()))
						.logout((logout) -> logout.permitAll());
						

		http.exceptionHandling().accessDeniedPage("/denied-page");
		return http.build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers("/css/**", "/fonts/**", "/images/**", "/js/**");
	}
}
