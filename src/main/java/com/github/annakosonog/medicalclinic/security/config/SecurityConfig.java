package com.github.annakosonog.medicalclinic.security.config;
import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
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
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        return username -> repository.findByEmail(username)
                .map(this::build)
                .orElseThrow(PatientNotFoundException::new);
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
                .antMatchers(HttpMethod.POST,"/patients").anonymous()
                .antMatchers(HttpMethod.GET,"/patients").hasRole(Role.ADMIN.name().toUpperCase())
                .antMatchers("/h2-console/**").hasRole(Role.ADMIN.name().toUpperCase())
                .antMatchers("/patients/**").hasRole(Role.PATIENT.name().toUpperCase())
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
