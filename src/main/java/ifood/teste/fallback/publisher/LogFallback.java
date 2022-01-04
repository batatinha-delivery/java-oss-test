package ifood.teste.fallback.publisher;

import ifood.teste-oss-java.commons.log.ContextLogger;
import ifood.teste-oss-java.commons.log.ContextLoggerFactory;
import ifood.teste-oss-java.fallback.model.FallbackResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class LogFallback<T> implements FallbackHandler<T>{

    private final ContextLogger log = ContextLoggerFactory.getLogger(getClass());

    public FallbackResult handle(final T value) {
        // generate an identifier to fallback execution
        final String publishIdentifier = UUID.randomUUID().toString();

        // create a fallback result
        final FallbackResult fallbackResult = new FallbackResult(publishIdentifier);

        // log some information for fallback
        log.info("fallback execute",
                Map.of("value", value.toString(), "result", fallbackResult.toString()));

        return fallbackResult;
    }
}
