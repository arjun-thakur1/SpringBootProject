package Work1.Project1.Package.configuration;

import Work1.Project1.Package.interceptor.ImplementInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class InterceptorAppConfig  extends WebMvcConfigurerAdapter {
        @Autowired
        ImplementInterceptor implementInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {

            registry.addInterceptor(implementInterceptor);
        }
}

