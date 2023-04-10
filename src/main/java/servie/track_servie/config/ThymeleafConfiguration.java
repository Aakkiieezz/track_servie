package servie.track_servie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfiguration
{
    @Bean
    public SpringTemplateEngine templateEngine()
    {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver()
    {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        viewResolver.setViewNames(new String[] {"*.html"});
        viewResolver.setCache(false);
        viewResolver.setRedirectContextRelative(false);
        viewResolver.setContentType("text/html; charset=UTF-8");
        viewResolver.setExcludedViewNames(new String[] {"error", "error/*", "redirect:*"});
        viewResolver.setRedirectHttp10Compatible(false);
        // Set the Thymeleaf servlet flag
        viewResolver.addStaticVariable("thymeleafServletRequest", true);
        // viewResolver.addStaticVariable("thymeleafServlet", "thymeleaf");
        return viewResolver;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver()
    {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        // resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // resolver.setCacheable(false);
        return templateResolver;
    }
}