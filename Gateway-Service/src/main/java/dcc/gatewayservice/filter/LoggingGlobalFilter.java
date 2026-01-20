package dcc.gatewayservice.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = Logger.getLogger(LoggingGlobalFilter.class.getName());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        logger.info("============================================");
        logger.info(" GATEWAY REQUEST START");
        logger.info("============================================");
        logger.info(" Path: " + request.getPath().value());
        logger.info(" Method: " + request.getMethod());
        logger.info(" URI: " + request.getURI());

        // Log Authorization header
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && !authHeader.isEmpty()) {
            logger.info(" Authorization Header: " + authHeader.substring(0, Math.min(30, authHeader.length())) + "...");
        } else {
            logger.warning("⚠    NO Authorization Header found!");
        }

        // Log Content-Type
        String contentType = request.getHeaders().getFirst("Content-Type");
        logger.info(" Content-Type: " + (contentType != null ? contentType : "Not set"));

        // Log all headers for debugging
        logger.info(" All Headers:");
        request.getHeaders().forEach((key, value) -> {
            logger.info("   " + key + ": " + value);
        });

        logger.info("============================================");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info(" *********  GATEWAY RESPONSE: " + exchange.getResponse().getStatusCode());
            logger.info("============================================");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Highest priority
    }
}