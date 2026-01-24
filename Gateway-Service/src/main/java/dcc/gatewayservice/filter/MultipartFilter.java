package dcc.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class MultipartFilter implements GlobalFilter, Ordered {

    private static final Logger logger = Logger.getLogger(MultipartFilter.class.getName());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        if (headers.getContentType() != null
                && headers.getContentType().toString().contains("multipart/form-data")) {

            logger.info("🔧 Multipart request detected - removing Content-Length header");


            ServerHttpRequest modifiedRequest = request.mutate()
                    .headers(h -> h.remove(HttpHeaders.CONTENT_LENGTH))
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}