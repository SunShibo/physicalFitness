package com.ichzh.physicalFitness.security;

import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

//@Configuration
//@EnableAuthorizationServer
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {
//	@Autowired @Qualifier("dataSource")DataSource dataSource;
//	@Autowired 
////	@Qualifier("authenticationManagerBean")
//	AuthenticationManager authenticationManager;
//	
//	@Bean
//    public JdbcClientDetailsService clientDetailsService() {
//        return new JdbcClientDetailsService(dataSource);
//    }
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }
//
//    @Bean
//    public ApprovalStore approvalStore() {
//        return new JdbcApprovalStore(dataSource);
//    }
//
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices() {
//        return new JdbcAuthorizationCodeServices(dataSource);
//    }
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.withClientDetails(clientDetailsService());
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//    	oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints
//                .approvalStore(approvalStore())
//                .authorizationCodeServices(authorizationCodeServices())
//                .tokenStore(tokenStore())
//                .authenticationManager(authenticationManager);
//    }
}
