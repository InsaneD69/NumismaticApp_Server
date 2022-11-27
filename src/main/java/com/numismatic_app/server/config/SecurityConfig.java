package com.numismatic_app.server.config;

import com.numismatic_app.server.security.AuthProviderImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig  implements WebMvcConfigurer {

@Autowired

private AuthProviderImpl authProvider;


    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/acc/new").anonymous()
                .antMatchers("/search/countries","acc/login","collection/new,","collection/get").authenticated()
                .and().csrf().disable()
               .logout()
                .and().httpBasic();
        return http.build();

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4000")
                .allowedMethods("*");
    }


    protected void configure(AuthenticationManagerBuilder auth)  {

        auth.authenticationProvider(authProvider);

    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
