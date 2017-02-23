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
        registry.addViewController("/pending").setViewName("/credentials/pending");
        registry.addViewController("/activate").setViewName("/credentials/activate");

        registry.addViewController("/member").setViewName("/pages/member");
        registry.addViewController("/admin").setViewName("/pages/admin");
    }
}
