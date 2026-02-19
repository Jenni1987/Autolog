package com.autolog.autolgapi;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
	
    @Bean
    UserDetailsManager users(DataSource dataSource, PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery(
            "SELECT EMAIL_USA as username, PASSWORD_USA as password, true as enabled FROM user_autolog WHERE EMAIL_USA = ?"
        );

        manager.setAuthoritiesByUsernameQuery(
            "SELECT usa.EMAIL_USA as username, "
            + "COALESCE(roa.ROLCODE_ROA, 'basic') as authority "
            + "FROM user_autolog usa "
            + "LEFT JOIN userrol_autolog ura ON ura.IDUSER_URA = usa.IDUSER_USA "
            + "LEFT JOIN rol_autolog roa ON roa.IDROL_ROA = ura.IDROL_URA "
            + "WHERE usa.EMAIL_USA = ?"
        );

        return manager;
    }
    
    
	@Bean
	PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/login", "/css/**", "/js/**", "/img/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(login -> login
	                .loginPage("/login")
	                .loginProcessingUrl("/loginprocess")
	                .defaultSuccessUrl("/index", true)
	                .failureUrl("/login?error=true")
	                .permitAll()
	            )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	            .permitAll()
	        );

	    return http.build();
	}
	
}
