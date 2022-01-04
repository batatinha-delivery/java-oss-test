package ifood.teste.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfiguration {

    // CircuitBreaker for persistence databases use cases
    @Bean
    public CircuitBreaker persistenceCircuitBreaker(final CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("persistenceCircuitBreaker");
    }

     // CircuitBreaker for read databases use cases
    @Bean
    public CircuitBreaker readCircuitBreaker(final CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("readCircuitBreaker");
    }

    // CircuitBreaker for fallbacks use cases
    @Bean
    public CircuitBreaker fallbackCircuitBreaker(final CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("fallbackCircuitBreaker");
    }

}