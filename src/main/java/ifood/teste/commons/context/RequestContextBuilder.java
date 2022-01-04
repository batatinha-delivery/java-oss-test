package ifood.teste.commons.context;

import com.ifood.audit.interfaces.AuditHeaders;
import com.ifood.audit.interfaces.HttpRequestAuditHeaders;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class RequestContextBuilder {

    // create RequestAttributes from @RequestHeader org.springframework.http.HttpHeaders
    public static HttpRequestContext createFrom(final HttpHeaders httpHeaders) {
        final Map<String, Object> headers = new HashMap<>();

        //populate values from mapped headers from RequestAttributes.Headers
        Stream.of(HttpRequestContext.IFoodHeaders.values())
                .forEach(header -> {
                    final String headerValue = httpHeaders.getFirst(header.getHeaderName());
                    Optional.ofNullable(headerValue)
                            .map(value -> headers.put(header.getProperty(), headerValue));
                });

        // for http endpoints, get headers (like ifood requestId, sessionId, ...) to enter information from requests in the audit
        final AuditHeaders auditHeaders = HttpRequestAuditHeaders.INSTANCE.getAuditHeaders(httpHeaders);

        return new HttpRequestContext(headers, auditHeaders);
    }

}
