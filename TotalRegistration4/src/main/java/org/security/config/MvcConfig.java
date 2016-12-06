package org.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author newlife
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/login").setViewName("/credentials/login");
        registry.addViewController("/pending").setViewName("/credentials/pending");
        registry.addViewController("/password").setViewName("/credentials/password");
        registry.addViewController("/member").setViewName("/pages/member");
        registry.addViewController("/admin").setViewName("/pages/admin");
        registry.addViewController("/invalid").setViewName("/error/invalid");
        registry.addViewController("/403").setViewName("/error/403");
    }
}
