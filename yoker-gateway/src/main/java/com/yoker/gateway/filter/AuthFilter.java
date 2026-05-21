package com.yoker.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoker.common.result.Result;
import com.yoker.common.result.ResultCode;
import com.yoker.gateway.config.WhiteListConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final WhiteListConfig whiteListConfig;
//    @Value("${white-list.urls}")
//    private List<String> whiteList;


    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 白名单放行
        for (String url : whiteListConfig.getUrls()) {
            if (pathMatcher.match(url, path)) {
                return chain.filter(exchange);
            }
        }

        // 获取 Token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return unauthorized(exchange.getResponse());
        }

        // 验证 Redis 中是否存在
        String realToken = token.replace("Bearer ", "");
        Boolean hasKey = redisTemplate.hasKey("token:" + getUsernameFromToken(realToken));
        if (Boolean.FALSE.equals(hasKey)) {
            return unauthorized(exchange.getResponse());
        }

        return chain.filter(exchange);
    }

    /**
     * 简单解析用户名（不验证签名，网关只做存在性校验）
     */
    private String getUsernameFromToken(String token) {
        try {
            String payload = token.split("\\.")[1];
            byte[] decoded = java.util.Base64.getDecoder().decode(payload);
            String json = new String(decoded);
            return objectMapper.readTree(json).get("sub").asText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 返回 401
     */
    private Mono<Void> unauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Result<Void> result = Result.fail(ResultCode.UNAUTHORIZED);
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            bytes = new byte[0];
        }
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
