package com.github.annakosonog.medicalclinic.security;

import com.github.annakosonog.medicalclinic.exception.DataNotFoundException;
import com.github.annakosonog.medicalclinic.model.Role;
import com.github.annakosonog.medicalclinic.model.UserData;
import com.github.annakosonog.medicalclinic.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        return username -> repository.findByEmail(username)
                .map(this::build)
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/patients").anonymous()
                .antMatchers(HttpMethod.GET, "/patients").hasRole(Role.ADMIN.name().toUpperCase())
                .antMatchers("/h2-console/**").hasRole(Role.ADMIN.name().toUpperCase())
                .antMatchers("/patients/**").hasRole(Role.PATIENT.name().toUpperCase())
                .antMatchers(HttpMethod.POST, "/visits").hasRole(Role.ADMIN.name().toUpperCase())
                .antMatchers("/visits/**").hasRole(Role.PATIENT.name().toUpperCase())
                .antMatchers(HttpMethod.GET, "/visits").hasRole(Role.PATIENT.name().toUpperCase())
                .antMatchers(HttpMethod.POST, "/doctors").hasRole(Role.ADMIN.name().toUpperCase())
                .antMatchers(HttpMethod.GET, "/doctors").hasRole(Role.ADMIN.name().toUpperCase())
                .antMatchers(HttpMethod.POST, "/facilities").hasRole(Role.ADMIN.name().toUpperCase())
                .antMatchers(HttpMethod.GET, "/facilities").authenticated()
                .antMatchers(HttpMethod.PATCH, "/doctors/**").hasRole(Role.DOCTOR.name().toUpperCase())
                .antMatchers("/other/**").permitAll()
                .anyRequest().denyAll()
                .and().build();
    }

    private UserDetails build(UserData userData) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userData.getRole().getAuthorityName());
        return User.builder()
                .username(userData.getEmail())
                .password(userData.getPassword())
                .authorities(simpleGrantedAuthority)
                .build();
    }
}
