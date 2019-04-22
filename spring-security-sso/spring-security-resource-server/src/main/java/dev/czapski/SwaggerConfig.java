package dev.czapski;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@EnableResourceServer
@Configuration
public class SwaggerConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(Collections.singletonList(oauthSecurityScheme()))
                .securityContexts(Collections.singletonList(securityContext()))
                ;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(
                        new SecurityReference(
                                "oauth",
                                new AuthorizationScope[]{}
                                )
                    )
                )
                .forPaths(PathSelectors.any())
                .build();
    }

    private SecurityScheme oauthSecurityScheme() {
        GrantType grantType = new ImplicitGrantBuilder()
                .loginEndpoint(new LoginEndpoint("http://localhost:8081/auth/oauth/authorize"))
                .build();

        return new OAuthBuilder()
            .name("oauth")
            .grantTypes(Collections.singletonList(grantType))
            .build();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }

    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder()
                .clientId("SampleClientId")
                .build();
    }
}
