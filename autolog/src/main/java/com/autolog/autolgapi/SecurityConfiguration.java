package com.autolog.autolgapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.autolog.connections.MySqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	public static InMemoryUserDetailsManager InMemory;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
		   .authorizeHttpRequests((authorize) -> authorize
				    			  .requestMatchers("/", "/login", "/loginprocess", "/js/**", "/css/**", "/img/**").permitAll() 
				    			  .anyRequest().authenticated()
				                 )
		   .formLogin(form -> form
				              .loginPage("/login")
				              .loginProcessingUrl("/loginprocess")
				              .defaultSuccessUrl("/index", true)
				              .permitAll()
				   )
		   .logout((logout) -> logout.logoutSuccessUrl("/login?logout").permitAll());		   
		
		return http.build();
	}
	
	@Bean
	InMemoryUserDetailsManager userDetailsService() {
		
		InMemory = new InMemoryUserDetailsManager();

		MySqlConnection objMySqlConnection = new MySqlConnection();
		
		objMySqlConnection.open();
		
		if(!objMySqlConnection.isError()) {
			ResultSet result = objMySqlConnection.executeSelect(
					"SELECT usa.EMAIL_USA AS USUARIO, usa.PASSWORD_USA AS PASS, "
					+ "GROUP_CONCAT(roa.ROLCODE_ROA ORDER BY roa.ROLCODE_ROA SEPARATOR ', ') AS ROLCODES "
					+ "FROM db_autolog.user_autolog usa "
					+ "LEFT JOIN db_autolog.userrol_autolog ura on ura.IDUSER_URA = usa.IDUSER_USA "
					+ "LEFT JOIN db_autolog.rol_autolog roa on roa.IDROL_ROA = ura.IDROL_URA "
					+ "group by 1,2;");
			try {
				while (result.next()) {  
					String roles = result.getString("ROLCODES");
					if(roles == null) {				
						InMemory.createUser(
							    User.builder()
							        .username(result.getString("USUARIO"))
							        .password(result.getString("PASS"))
							        .roles("basic")
							        .build()
							);
					} else {
						String[] rolesArray = Arrays.stream(roles.split(","))
	                            .map(String::trim)
	                            .toArray(String[]::new);
						InMemory.createUser(
							    User.builder()
							        .username(result.getString("USUARIO"))
							        .password(result.getString("PASS"))
							        .roles(rolesArray)
							        .build()
							);
					}
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		objMySqlConnection.close();
		return InMemory;
	}
	
	
}
