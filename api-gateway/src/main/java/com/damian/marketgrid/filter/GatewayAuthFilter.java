package com.damian.marketgrid.filter;

import com.damian.marketgrid.properties.AuthProperties;
import com.damian.marketgrid.dto.UserAuthDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayAuthFilter implements GlobalFilter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private final WebClient.Builder webClientBuilder;
    private final AuthProperties authProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String path = exchange.getRequest().getPath().value();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        return webClientBuilder.build()
                .get()
                .uri(authProperties.getService().getUserAuthDetailsEndpoint())
                .header(HttpHeaders.COOKIE, exchange.getRequest().getHeaders().getFirst(HttpHeaders.COOKIE))
                .retrieve()
                .bodyToMono(UserAuthDetails.class)
                .flatMap(userAuthDetails -> forwardRequestWithAdditionalUserHeaders(exchange, chain, userAuthDetails))
                .onErrorResume(e -> sendError(exchange, path, e));
    }

    private boolean isPublicPath(String path) {
        return authProperties.getPublicPaths().stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    private Mono<Void> forwardRequestWithAdditionalUserHeaders(ServerWebExchange exchange,
                                                     GatewayFilterChain chain,
                                                     UserAuthDetails userAuthDetails) {
        final String userIdField = "X-User-Id";
        final String userRolesField = "X-User-Roles";
        var newRequest = exchange.getRequest().mutate()
                .header(userIdField, userAuthDetails.id().toString())
                .header(userRolesField, String.join(",", userAuthDetails.roles()))
                .build();
        var newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange)
                .doFinally(signal -> {
                    exchange.getResponse().getHeaders().remove(userIdField);
                    exchange.getResponse().getHeaders().remove(userRolesField);
                });
    }

    private Mono<Void> sendError(ServerWebExchange exchange, String path, Throwable e) {
        log.error("Error acquiring user details for path {}: {}", path, e.getMessage(), e);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}

