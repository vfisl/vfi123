package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter{

	    @Autowired
		private UserDetailsService jwtUserDetailsService; 
//	    @Autowired
	    private JwtRequestFilter jwtRequestFilter;
	    @Autowired
	    private AuthenticationEntryPoint jwtAuthenticationEntryPoint;
		
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
		}

	    @Bean
	    @Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			// TODO Auto-generated method stub
			return super.authenticationManagerBean();
		}
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	    	return new BCryptPasswordEncoder();
	    }
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
			.authorizeRequests().antMatchers("authenticate","register")
			.permitAll()
			.anyRequest().authenticated()
			.and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		}
}
