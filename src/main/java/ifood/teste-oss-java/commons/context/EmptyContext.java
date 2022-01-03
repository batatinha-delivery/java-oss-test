package ifood.teste-oss-java.commons.context;

import com.ifood.audit.interfaces.AuditHeaders;

import java.util.HashMap;
import java.util.Map;

// Empty structure for context without request information
public class EmptyContext implements RequestContext {

    @Override
    public Map<String, Object> getValues() {
        return new HashMap<>();
    }

    @Override
    public AuditHeaders getAuditHeaders() {
        return new AuditHeaders();
    }
}
