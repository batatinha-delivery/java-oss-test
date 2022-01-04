package ifood.teste.commons.context;

import com.ifood.audit.interfaces.AuditHeaders;
import ifood.teste-oss-java.commons.log.ContextLog;

import java.util.Map;

// RequestContext interface to define context information to log standard or get request information in any process
// RequestContext extents ContextLog to prepare context structures for log standard
public interface RequestContext extends ContextLog {

    Map<String, Object> getValues();

    AuditHeaders getAuditHeaders();
}
