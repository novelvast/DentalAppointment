package com.microservice.gatewayservice.authorization;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;


@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {


    /**
     * 实现权限验证判断
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext authorizationContext) {


        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();

        //白名单路径直接放行
        if (pathMatcher.match("/**/login", uri.getPath())) {
            return Mono.just(new AuthorizationDecision(true));
        }
        if (pathMatcher.match("/**/register", uri.getPath())) {
            return Mono.just(new AuthorizationDecision(true));
        }
        //对应跨域的预检请求直接放行
        if(request.getMethod()== HttpMethod.OPTIONS){
            return Mono.just(new AuthorizationDecision(true));
        }
        // 全部验证
        return Mono.just(new AuthorizationDecision(true));
    }


}

