package edu.vanderbilt.finsta.security;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;


/**
 * 
 * 
 * Highly Insecure!!!!
 * 
 * 
 * 
 * @author jules
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	private final UserDetailsService userDetailsService;
	
	private final AuthorityRepository authorityRepository;
	
	private final UserRepository userRepository;

	public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder,
			UserDetailsService userDetailsService, AuthorityRepository authRepo, UserRepository userRepo) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userDetailsService = userDetailsService;

		this.userRepository = userRepo;
		this.authorityRepository = authRepo;
	}

	@PostConstruct
	public void init() {
		try {
			authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		} catch (Exception e) {
			throw new BeanInitializationException("Security configuration failed", e);
		}

		Authority adminAuthority = new Authority();
		adminAuthority.setName(AuthoritiesConstants.ADMIN);
		adminAuthority = authorityRepository.save(adminAuthority);

		User admin = new User();
		admin.setActivated(true);
		admin.setEmail("admin@doesnt.exist");
		admin.setLogin("admin");
		admin.setFirstName("Admin");
		admin.setLastName("Admin");
		admin.setPassword(passwordEncoder().encode("admin"));

		Set<Authority> authorities = new HashSet<>();

		authorities.add(adminAuthority);
		admin.setAuthorities(authorities);

		this.userRepository.save(admin);
		
		User user = new User();
		user.setActivated(true);
		user.setEmail("user@doesnt.exist");
		user.setLogin("user");
		user.setFirstName("Bobby");
		user.setLastName("Insecure");
		user.setPassword(passwordEncoder().encode("user"));

		authorities = new HashSet<>();
		user.setAuthorities(authorities);
		
		this.userRepository.save(user);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Base64PasswordEncoder();
	}

	@Bean
	public HttpFirewall firewall() {
		DefaultHttpFirewall fw = new DefaultHttpFirewall();
		fw.setAllowUrlEncodedSlash(true);
		return fw;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/**/*.{js,html}");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.formLogin()
			.loginProcessingUrl("/api/authentication").permitAll()
			.and()
			.logout().logoutUrl("/api/logout").permitAll()
			.and().headers().frameOptions().disable()
			.and().authorizeRequests()
			.antMatchers("/photo").authenticated()
			.antMatchers("/photo/**").authenticated()
			.antMatchers("/user").permitAll()
			.antMatchers("/management/**").permitAll();

	}
}
