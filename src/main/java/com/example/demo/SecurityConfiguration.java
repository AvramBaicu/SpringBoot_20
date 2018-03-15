package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SSUserDetailsService userDetailService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsServiceBean()throws Exception{
        return new SSUserDetailsService(userRepository);
    }
        @Override
    protected void configure(HttpSecurity http)throws Exception{
        http


                .authorizeRequests()
                .antMatchers("/")
                .access("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .httpBasic();

        http
                .csrf().disable();
        http
                .headers().frameOptions().disable();
    }


    @Override
            protected void configure(AuthenticationManagerBuilder auth)
        throws Exception{

        auth.inMemoryAuthentication().
                withUser("dave").password("begreat").roles("ADMIN").
                and().
                withUser("user").password("password").roles("USER");
        auth
                .userDetailsService(userDetailsServiceBean());

    }


    public SSUserDetailsService getUserDetailService() {
        return userDetailService;
    }

    public void setUserDetailService(SSUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
