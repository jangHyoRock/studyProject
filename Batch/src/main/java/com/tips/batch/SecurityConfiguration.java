package com.tips.batch;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CorsConfiguration config = new CorsConfiguration();
		
		config.addAllowedOrigin(CorsConfiguration.ALL);
		config.addAllowedMethod(CorsConfiguration.ALL);
		config.addAllowedHeader(CorsConfiguration.ALL);
		
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		
		configurationSource.registerCorsConfiguration("/**", config);
		
		http.httpBasic()
			.and().authorizeRequests()
			.anyRequest().permitAll()
			.and().cors().configurationSource(configurationSource)
			.and().csrf().disable();
	}
}