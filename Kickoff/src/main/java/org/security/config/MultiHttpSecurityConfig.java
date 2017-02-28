package org.security.config;

import javax.sql.DataSource;
import org.security.admin.AdminDetailsServiceAdapter;
import org.security.service.CurrentUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 *
 * @author newlife
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MultiHttpSecurityConfig {

    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${number.rememberme.seconds}")
        private int seconds;

        @Autowired
        private DataSource dataSource;

        @Autowired
        private CurrentUserDetailsService userDetailsService;

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

    @Configuration
    @Order(1)
    public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AdminDetailsServiceAdapter userDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            // @formatter:off         
            http
                    .antMatcher("/admin")
                    .antMatcher("/admin/**")
                    .formLogin().loginPage("/admin").successForwardUrl("/admin/panel").defaultSuccessUrl("/admin/panel")
                    .failureUrl("/admin?error").usernameParameter("email").passwordParameter("password").permitAll()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .anyRequest().permitAll()
                    .and()
                    .logout().logoutUrl("/admin/logout").logoutSuccessUrl("/admin?logout")
                    .invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll()
                    .and()
                    .exceptionHandling()
                    .and()
                    .sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true)
                    .and()
                    .sessionFixation().none();
            // @formatter:on
        }

        @Autowired
        public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
        }
    }
}
