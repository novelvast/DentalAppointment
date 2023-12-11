package com.microservice.personalinfoservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


/**
 * 资源服务配置类
 *
 * @author zhao
 * @date 2023/12/10
 */
@Configuration
@EnableResourceServer // 开启资源服务器功能
@EnableWebSecurity // 开启web访问安全
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private String sign_key = "test123"; // jwt签名密钥

    /**
     * 定义资源服务器向远程认证服务器发起请求，进行token校验
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        // 设置当前资源服务的资源id 与认证id保持一致  可以不设置
        resources.resourceId("personal-info-service")
                .tokenStore(tokenStore())
                .stateless(true);// ⽆状态设置
    }

    /**
     * 配置认证策略
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http // 设置session的创建策略（根据需要创建即可）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/api/patient/loadByUsername").permitAll() // TODO：只有auth服务能够访问
                .antMatchers("/api/**").authenticated() // api为前缀的请求需要认证
                .anyRequest().permitAll(); // 其他请求不认证
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


}


