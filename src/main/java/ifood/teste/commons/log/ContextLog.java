package ifood.teste.commons.log;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

public interface ContextLog {

    String CONTEXT_FIELD = "context"; // log field to additional information like ids, model fields or any other information for enrich the log
    String EXCEPTION_MESSAGE = "exception.message";
    String EXCEPTION_STACKTRACE = "exception.stacktrace";

    Map<String, Object> getValues();

    // get values map to log with context information
    default Map<String, Object> getValuesWithContextValue(final Map<String, String> context) {
        final Map<String, Object> values = getValuesOrEmpty();
        values.put(CONTEXT_FIELD, context);
        return values;
    }

    // get values map to log with context and throwable
    default Map<String, Object> getValuesWithException(final Map<String, String> context, final Throwable throwable) {
        final Map<String, Object> values = getValuesOrEmpty();

        if (context != null && !context.isEmpty()) {
            values.put(CONTEXT_FIELD, context);
        }

        if (throwable != null) {
            values.put(EXCEPTION_MESSAGE, throwable.getMessage());
            values.put(EXCEPTION_STACKTRACE, ExceptionUtils.getStackTrace(throwable));
        }

        return values;
    }

    default Map<String, Object> getValuesOrEmpty() {
        return getValues() == null ? new HashMap<>() : new HashMap<>(getValues());
    }

}
