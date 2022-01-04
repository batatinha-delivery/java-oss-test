package ifood.teste.fallback.publisher;

import ifood.teste.fallback.model.FallbackResult;

public interface FallbackHandler<T> {
    FallbackResult handle(T value);
}