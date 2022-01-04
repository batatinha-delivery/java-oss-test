package ifood.teste.commons.log;

import ifood.teste-oss-java.commons.context.EmptyContext;
import ifood.teste-oss-java.commons.context.RequestContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ContextLogger {

    private final Logger log;

    ContextLogger(final Class<?> clazz) {
        this.log = LogManager.getLogger(clazz);
    }

    public void info(final String message, final RequestContext requestContext) {
        log(Level.INFO, message, requestContext);
    }

    public void info(final String message, final RequestContext requestContext, final Map<String, String> context) {
        log(Level.INFO, message, requestContext, context);
    }

    public void info(final String message,final Map<String, String> context) {
        log(Level.INFO, message, context);
    }

    public void debug(final String message, final RequestContext requestContext) {
        log(Level.DEBUG, message, requestContext);
    }

    public void debug(final String message, final RequestContext requestContext, final Map<String, String> context) {
        log(Level.DEBUG, message, requestContext, context);
    }

    public void warn(final String message, final RequestContext requestContext) {
        log(Level.WARN, message, requestContext);
    }

    public void warn(final String message, final Throwable throwable) {
        log(Level.WARN, message, null, throwable);
    }

    public void warn(final String message, final RequestContext requestContext, final Map<String, String> context) {
        log(Level.WARN, message, requestContext, context);
    }

    public void warn(final String message, final RequestContext requestContext, final Map<String, String> context, final Throwable throwable) {
        log(Level.WARN, message, requestContext, context, throwable);
    }

    public void error(final String message, final Map<String, String> context, final Throwable throwable) {
        log(Level.ERROR, message, context, throwable);
    }

    public void error(final String message, final Throwable throwable) {
        log(Level.ERROR, message, null, throwable);
    }

    public void log(final Level level, final String message, final RequestContext requestContext) {
        this.log.log(level, message, getValuesOrEmpty(requestContext));
    }

    public void log(final Level level, final String message, final RequestContext requestContext, final Map<String, String> context) {
        this.log.log(level, message, getValuesOrEmpty(requestContext, context));
    }

    public void log(final Level level, final String message, final RequestContext requestContext, final Map<String, String> context, final Throwable throwable) {
        this.log.log(level, message, getValuesOrEmpty(requestContext, context, throwable));
    }

    public void log(final Level level, final String message, final Map<String, String> context) {
        this.log.log(level, message, new EmptyContext().getValuesWithContextValue(context));
    }

    public void log(final Level level, final String message, final Map<String, String> context, final Throwable throwable) {
        this.log.log(level, message, new EmptyContext().getValuesWithException(context, throwable));
    }

    private Map<String, Object> getValuesOrEmpty(final RequestContext requestContext) {
        return requestContext == null ?  new HashMap<>() : requestContext.getValues();
    }

    private Map<String, Object> getValuesOrEmpty(final RequestContext requestContext, final Map<String, String> context) {
        return requestContext == null ?  new HashMap<>() : requestContext.getValuesWithContextValue(context);
    }

    private Map<String, Object> getValuesOrEmpty(final RequestContext requestContext, final Map<String, String> context, final Throwable throwable) {
        return requestContext == null ?  new HashMap<>() : requestContext.getValuesWithException(context, throwable);
    }
}
