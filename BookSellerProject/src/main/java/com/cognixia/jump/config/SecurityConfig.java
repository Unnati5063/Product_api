package com.cognixia.jump.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cognixia.jump.filter.JWTfilter;
import com.cognixia.jump.service.MyUserDetailsService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JWTfilter jwtfilter;
	
	@Override
	protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
		
		auth.inMemoryAuthentication()
			.withUser("admin")
			.password(passwordEncoder().encode("admin123"))
			.authorities("ADMIN");
		auth.userDetailsService( userDetailsService );
	}
	
	@Override
	protected void configure( HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/v3/api-docs/**").permitAll()
			.antMatchers("/swagger-ui*/**").permitAll()
			.antMatchers("/api/register").permitAll()
			.antMatchers("/api/authenticate").permitAll()
			.antMatchers("/api/user/cart").permitAll()
			.antMatchers("/api/change/password").permitAll()
			.antMatchers("/api/user/cart/{id}").permitAll()
			.antMatchers("/api/user/addToCart/{id}").permitAll()
			.antMatchers("/api/user").hasRole("ADMIN")
			.antMatchers("/api/user/{name}").hasRole("ADMIN")
			.antMatchers("/api/book/{id}").hasRole("ADMIN")
			.antMatchers("/api/books").permitAll()
			.antMatchers("/api/books/add").hasRole("ADMIN")
			.antMatchers("/api/cart").hasRole("ADMIN")
			.anyRequest().authenticated() 
			.and().sessionManagement()
			.sessionCreationPolicy( SessionCreationPolicy.STATELESS ); 
		http.addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
	}

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
}