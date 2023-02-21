package com.github.annakosonog.medicalclinic.security.config;

import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.repository.PatientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PatientRepository repository) {
        return username -> repository.findByEmail(username)
                .map(this::build)
                .orElseThrow(PatientNotFoundException::new);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .httpBasic()
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/patients").anonymous()
                .antMatchers("/h2-console").authenticated()
                .antMatchers(HttpMethod.GET, "/patients").authenticated()
                .antMatchers("/patients/{email}").authenticated()
                .anyRequest().authenticated()
                .and()
                .build();
    }


    private UserDetails build(Patient patient) {
        return User.builder()
                .username(patient.getEmail())
                .password(patient.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
