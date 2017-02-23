package org.security.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 *
 * @author newlife
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${number.rememberme.seconds}")
    private int seconds;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http
                .formLogin().loginPage("/login").successForwardUrl("/").defaultSuccessUrl("/")
                .failureUrl("/login?error").usernameParameter("email").passwordParameter("password").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login/**", "/pending/**", "/activate/**",
                        "/register/**", "/reset/**", "/newpassword/**",
                        "/css/**", "/js/**", "/images/**", "/fonts/**",
                        "/favicon.ico").permitAll()
                .antMatchers("/member/**").hasAuthority("ROLE_MEMBER")
                //.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().fullyAuthenticated()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false)
                .and()
                .sessionFixation().none()
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService).tokenValiditySeconds(seconds) // 2 minutes
                .rememberMeParameter("remember-me")
                .and()
                .csrf();
        // @formatter:on
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }    
}
