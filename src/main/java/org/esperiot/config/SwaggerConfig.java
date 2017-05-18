package org.esperiot.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements EnvironmentAware {

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .paths(regex(DEFAULT_INCLUDE_PATTERN))
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
    }

    /**
     * API Info as it appears on the swagger-ui page.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
            propertyResolver.getProperty("title"),
            propertyResolver.getProperty("description"),
            propertyResolver.getProperty("version"),
            propertyResolver.getProperty("termsOfServiceUrl"),
            propertyResolver.getProperty("contact"),
            propertyResolver.getProperty("license"),
            propertyResolver.getProperty("licenseUrl"));
    }
}
