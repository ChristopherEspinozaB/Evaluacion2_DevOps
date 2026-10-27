package cl.supletanes.supletanes_app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class ProxyHeadersConfig {
    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new ForwardedHeaderFilter());
        filterRegBean.setOrder(0);
        return filterRegBean;
    }
}