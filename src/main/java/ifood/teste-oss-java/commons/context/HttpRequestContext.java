package ifood.teste-oss-java.commons.context;

import com.ifood.audit.interfaces.AuditHeaders;

import java.util.Map;
import java.util.Optional;

// RequestContext structure to HttpRequest information
// it`s useful to transport headers information for implementations
public class HttpRequestContext implements RequestContext {

    private final Map<String, Object> values;
    private final AuditHeaders auditHeaders;

    public HttpRequestContext(final Map<String, Object> values) {
        this(values, null);
    }

    public HttpRequestContext(final Map<String, Object> values, final AuditHeaders auditHeaders) {
        this.values = values;
        this.auditHeaders = auditHeaders;
    }

    @Override
    public Map<String, Object> getValues() {
        return this.values;
    }

    @Override
    public AuditHeaders getAuditHeaders() {
        return Optional.ofNullable(this.auditHeaders).orElse(new AuditHeaders());
    }

    Object getValue(final IFoodHeaders header) {
        return this.values.get(header.getProperty());
    }

    // Define Http Ifood header and their property name
    enum IFoodHeaders {
        IFOOD_REQUEST_ID("X-iFood-Request-Id", "request_id"),
        IFOOD_SESSION_ID("X-IFood-Session-Id", "session_id"),
        CLIENT_CONNECTION("X-iFood-Client-Connection", "client_connection"),
        ACCOUNT_ID("X-iFood-Account-Id", "account_id"),
        MERCHANT_ID("X-iFood-Merchant-Id", "merchant_id"),
        DRIVER_ID("X-iFood-Driver-Id", "driver_id"),
        ORDER_ID("X-iFood-Order-Id", "order_id"),
        ;

        private final String headerName;
        private final String property; //used for log

        IFoodHeaders(String header, String property) {
            this.headerName = header;
            this.property = property;
        }

        public String getHeaderName() {
            return headerName;
        }

        public String getProperty() {
            return property;
        }
    }
}
