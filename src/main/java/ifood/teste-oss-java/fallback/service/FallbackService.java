package ifood.teste-oss-java.fallback.service;

import ifood.teste-oss-java.fallback.model.FallbackResult;
import ifood.teste-oss-java.fallback.publisher.FallbackHandler;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FallbackService<T> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final FallbackHandler<T> fallbackHandler;
    private final CircuitBreaker fallbackCircuitBreaker;

    public FallbackService(
            final FallbackHandler<T> fallbackHandler,
            final CircuitBreaker fallbackCircuitBreaker) {
        this.fallbackHandler = fallbackHandler;
        this.fallbackCircuitBreaker = fallbackCircuitBreaker;
    }

    public void publish(final T body) {
        // try to call fallback
        Try.ofSupplier(CircuitBreaker.decorateSupplier(fallbackCircuitBreaker, () -> {
                    // call fallback publish
                    final FallbackResult result = this.fallbackHandler.handle(body);

                    // log an identifier after fallback executed
                    log.info("Published fallback: result: {} - body: {}", result, body);

                    // returning fallback result
                    return Optional.of(result);
                }))
                .recover(throwable -> {
                    // error log on publish and signals the fallback
                    log.error("Failure execute fallback for: {}.", body, throwable);

                    // returning empty
                    return Optional.empty();
                });
    }
}