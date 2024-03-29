package com.codegym.inote.configuration;

import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomSuccessHandler customSuccessHandler;

    @Autowired
    UserService userService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public WebSecurity webSecurity(){
        return  new WebSecurity();
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/restful/**");
        http.httpBasic().authenticationEntryPoint(restServicesEntryPoint());
        http.authorizeRequests()
                .antMatchers("/homepage",
                        "/doLogin",
                        "/register",
                        "/restful/register",
                        "/restful/login",
                        "/confirm-account/**").permitAll()
                .antMatchers("/note/notes/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/note/create/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/note/edit/{id}/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/note/delete/{id}/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/note/view/{id}/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/noteType/noteTypeList/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/noteType/create/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/noteType/edit/{id}/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/noteType/delete/{id}/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/noteType/view/{id}/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers(HttpMethod.GET, "/restful/users").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/restful/**").access("hasRole('ROLE_USER')")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll().loginProcessingUrl("/doLogin").successHandler(this.customSuccessHandler)
                .usernameParameter("username").passwordParameter("password")
                .and().csrf()
                .and().exceptionHandling().accessDeniedPage("/Access_Denied")
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
    }
}
