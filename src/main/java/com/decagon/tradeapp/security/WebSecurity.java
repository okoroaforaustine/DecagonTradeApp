/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.security;

import com.decagon.tradeapp.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author austine.okoroafor
 */
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    
   
	@Autowired
	private AppUserServiceImpl userDetailsService;
	
	@Autowired
	private AutowireCapableBeanFactory beanFactory;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		JWTAuthenticationFilter jWTAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager());
		JWTAuthorizationFilter jWTAuthorizationFilter = new JWTAuthorizationFilter(authenticationManager());
		
		beanFactory.autowireBean(jWTAuthenticationFilter);
		beanFactory.autowireBean(jWTAuthorizationFilter);
		http.cors().and().csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
            .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(jWTAuthorizationFilter)
            .addFilter(jWTAuthenticationFilter)
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
       return super.authenticationManagerBean();
	}
    
    
    
}
