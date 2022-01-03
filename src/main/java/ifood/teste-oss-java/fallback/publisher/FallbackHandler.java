package ifood.teste-oss-java.fallback.publisher;

import ifood.teste-oss-java.fallback.model.FallbackResult;

public interface FallbackHandler<T> {
    FallbackResult handle(T value);
}