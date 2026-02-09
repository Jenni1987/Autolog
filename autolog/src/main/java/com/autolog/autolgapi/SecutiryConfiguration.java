package com.autolog.autolgapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity
public class SecutiryConfiguration {

	public static InMemoryUserDetailsManager InMemory;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
		   .authorizeHttpRequests((authorize) -> authorize
			//Activa la gestión de permisos por URL. authorize permitirá configurar rutas.	   
				    			  .requestMatchers("/", "/login", "/loginprocess").permitAll() 
				    			  //.requestMatchers("/admin").hasRole("administrador")
				    			  .anyRequest().authenticated()
				                  //Toda petición no especificada arriba requiere estar logueado.
				                 )
		   .httpBasic(withDefaults())
		   //Activa la autenticación HTTP Basic.
		   //Muy útil para Postman o apps REST.

		   .formLogin(form -> form
				              .loginPage("/login")
				              .loginProcessingUrl("/loginprocess")
				              //Endpoint donde se envían las credenciales.
				              //No necesitas implementar nada: Spring Security lo 
				              //intercepta automáticamente.
				              .defaultSuccessUrl("/index", true)
				              //para indicar a dónde se redirige el usuario después de iniciar sesión.
				              .permitAll()
				              //Permite acceder a login y procesado de login sin autenticación.
				   )
		   .logout((logout) -> logout.logoutSuccessUrl("/login?logout").permitAll());
			//Tras cerrar sesión → redirige a /loggin?logout.
			//.permitAll() deja que cualquier usuario pueda hacer logout.
		   
		
		return http.build();
	}
	
	@Bean
	InMemoryUserDetailsManager userDetailsService() {
	//Spring usará este bean como proveedor de usuarios.
	//Va a devolver un InMemoryUserDetailsManager con dos usuarios.
		
		@SuppressWarnings("deprecation")
		UserDetails user1 = User.withDefaultPasswordEncoder()
				                .username("pepito")
				                .password("1234")
				                .roles("usuario")
				                .build();
		
		@SuppressWarnings("deprecation")
		UserDetails user2 = User.withDefaultPasswordEncoder()
				                .username("pepita")
				                .password("5678")
				                .roles("usuario","administrador")
				                .build();
		//withDefaultPasswordEncoder() usa un encoder básico (solo para pruebas).
		//Por eso aparece @SuppressWarnings("deprecation").
		
		@SuppressWarnings("deprecation")
		UserDetails user3 = User.withDefaultPasswordEncoder()
				                .username("Juan")
				                .password("PepeJuan123")
				                .roles("Inspector")
				                .build();

		@SuppressWarnings("deprecation")
		UserDetails user4 = User.withDefaultPasswordEncoder()
				                .username("Pedro")
				                .password("1999")
				                .roles("Estratega")
				                .build();
		
		@SuppressWarnings("deprecation")
		UserDetails user5 = User.withDefaultPasswordEncoder()
				                .username("Marta")
				                .password("8888")
				                .roles("Estratega", "Inspector")
				                .build();
		
		InMemory = new InMemoryUserDetailsManager();
		// Crea un InMemoryUserDetailsManager vacío.
		
		InMemory.createUser(user1);
		InMemory.createUser(user2);
		InMemory.createUser(user3);
		InMemory.createUser(user4);
		InMemory.createUser(user5);
		//Registra los usuarios dentro del gestor de usuarios.
		
		return InMemory;
		//Ahora Spring usará estos usuarios para autenticar.
	}
	
	
}
