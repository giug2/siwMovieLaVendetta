package it.uniroma3.siw.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.model.Credentials.DEFAULT_ROLE;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
//public  class WebSecurityConfig {
	public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
                .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().and().cors().disable()
                .authorizeHttpRequests()
                //.requestMatchers("/**").permitAll()
                // chiunque (autenticato o no) puÃ² accedere alle pagine index, login, register, ai css, ai js, a bootstrap e alle immagini
                .requestMatchers(HttpMethod.GET,"/","/index","/register","/css/**", "/images/**", "favicon.ico", "/movie/**", "/movies/**", "/artist/**", "/js/**", "/error", "/vendor/**", "/fragments/**", "/files/**").permitAll()
        		// chiunque (autenticato o no) puÃ² mandare richieste POST al punto di accesso per login e register e find
                .requestMatchers(HttpMethod.POST,"/register", "/login", "/find").permitAll()

                //solo chi Ã¨ autenticato come amministratore puÃ² mandare richieste GET e POST a URL che iniziano con '/admin/..'
                .requestMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority(ADMIN_ROLE)
                .requestMatchers(HttpMethod.POST,"/admin/**").hasAnyAuthority(ADMIN_ROLE)

                .requestMatchers(HttpMethod.GET,"/user/**").hasAnyAuthority(DEFAULT_ROLE, ADMIN_ROLE)
                .requestMatchers(HttpMethod.POST,"/user/**").hasAnyAuthority(DEFAULT_ROLE, ADMIN_ROLE)
        		// tutti gli utenti autenticati possono accedere alle pagine rimanenti
                .anyRequest().authenticated()
                // LOGIN: qui definiamo il login
                .and().formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/login?error=true")
                // LOGOUT: qui definiamo il logout
                .and()
                .logout()
                // il logout Ã¨ attivato con una richiesta GET a "/logout"
                .logoutUrl("/logout")
                // in caso di successo, si viene reindirizzati alla home
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .clearAuthentication(true).permitAll();
        return httpSecurity.build();
    }
}