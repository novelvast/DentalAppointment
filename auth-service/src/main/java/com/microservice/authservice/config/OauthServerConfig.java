package com.microservice.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 认证服务配置类（Oauth2 server的配置类）
 *
 * @author zhao
 * @date 2023/12/10
 */
@EnableAuthorizationServer //开启认证服务器功能
@Configuration
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    private final String sign_key = "test123";

    /**
     * 配置api接口的访问权限
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);

        // 打开endpoints访问接⼝的开关
        security.allowFormAuthenticationForClients()    // 允许客户端表单认证
                .tokenKeyAccess("permitAll()")          // 开启端⼝/oauth/token_key的访问权限（允许）
                .checkTokenAccess("permitAll()");       // 开启端⼝/oauth/check_token的访问权限（允许）
    }

    /**
     * 客户端详情配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);

        clients.inMemory()          // 客户端信息存储在什么地⽅，此处暂配置为存储在内存中
                .withClient("client_patient") // 添加⼀个client配置,指定其client_id
                .secret("patient")  // 指定客户端的密码
                .resourceIds("personal-info-service") // 指定客户端 所能访问资源id清单，此处的资源id是需要在具体的资源服务器上也配置⼀样
                .authorizedGrantTypes("password", "refresh_token")// 认证类型/令牌颁发模式
                .scopes("all");     // 客户端的权限范围
    }

    /**
     * 配置token令牌管理相关
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);

        endpoints.tokenStore(tokenStore()) // 指定token的存储⽅法
                .tokenServices(authorizationServerTokenServices()) // token服务的⼀个描述，可以认为是token⽣成细节的描述，⽐如有效时间多少等
                .authenticationManager(authenticationManager) // 指定认证管理器，随后注⼊⼀个到当前类使⽤即可
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

    }

    /**
     * 创建tokenStore对象，指明token以什么形式存储
     */
    public TokenStore tokenStore() {
        //return new InMemoryTokenStore();      // 在内存中存储
        return new JwtTokenStore(jwtAccessTokenConverter());    // 使⽤jwt令牌存储
    }

    /**
     * jwt令牌转换器
     */
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        jwtAccessTokenConverter.setSigningKey(sign_key); // 签名密钥
        jwtAccessTokenConverter.setVerifier(new MacSigner(sign_key)); // 验证时使⽤的密钥，和签名密钥保持⼀致
        return jwtAccessTokenConverter;
    }


    /**
     * 获取⼀个token服务对象（该对象描述了token有效期等信息）
     */
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        // 使⽤默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());   // 添加使用jwt令牌
        defaultTokenServices.setSupportRefreshToken(true); // 开启令牌刷新
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setAccessTokenValiditySeconds(3600*2); // 设置令牌有效时间（2个⼩时）
        defaultTokenServices.setRefreshTokenValiditySeconds(3600*24*3); // 设置刷新令牌的有效时间(3天)
        return defaultTokenServices;
    }

}

