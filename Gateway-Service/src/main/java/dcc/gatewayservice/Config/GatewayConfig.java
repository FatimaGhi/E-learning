package dcc.gatewayservice.Config;

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;

@Configuration
public class GatewayConfig {
    @Bean
    public DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator(
            ReactiveDiscoveryClient reactiveDiscoveryClient) {

        DiscoveryLocatorProperties properties = new DiscoveryLocatorProperties();
        properties.setLowerCaseServiceId(true);

        return new DiscoveryClientRouteDefinitionLocator(reactiveDiscoveryClient, properties);
    }
}
