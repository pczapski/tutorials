package org.baeldung.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;


@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServerConfig(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("SampleClientId")
            .secret(passwordEncoder.encode("secret"))
            .authorizedGrantTypes("authorization_code", "implicit")
            .scopes("user_info")
            .autoApprove(true)
            .redirectUris(
                    "http://localhost:8082/ui/login",
                    "http://localhost:8083/ui2/login",
                    "http://localhost:8082/login",
                    "http://www.example.com/",
                    "http://localhost:8010/webjars/springfox-swagger-ui/oauth2-redirect.html"
            )
        // .accessTokenValiditySeconds(3600)
        ; // 1 hour
    }
}
