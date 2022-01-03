package ifood.teste-oss-java.commons.log;

public class ContextLoggerFactory {

    public static ContextLogger getLogger(final Class<?> clazz) {
        return new ContextLogger(clazz);
    }
}