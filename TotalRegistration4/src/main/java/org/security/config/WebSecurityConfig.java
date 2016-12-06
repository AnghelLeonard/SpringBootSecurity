package org.security.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                formLogin().loginPage("/login").defaultSuccessUrl("/home").failureUrl("/login?error").usernameParameter("email").passwordParameter("password").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/password/**", "/newpassword/**", "/reset/**", "/activate/**", "/home/**", "/login/**", "/pending/**", "/register/**",
                        "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .antMatchers("/member/**").hasAuthority("ROLE_MEMBER")
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().fullyAuthenticated()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .sessionManagement().invalidSessionUrl("/invalid").maximumSessions(1).maxSessionsPreventsLogin(false)
                .and()
                .sessionFixation().none()
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService).tokenValiditySeconds(120) // 2 minutes
                .rememberMeParameter("remember-me")
                .and()
                .csrf().disable();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
