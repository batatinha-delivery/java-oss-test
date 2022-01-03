package ifood.teste-oss-java.audit;

import com.ifood.audit.interfaces.AuditHeaders;
import com.ifood.audit.interfaces.BusinessAuditPayload;
import com.ifood.audit.interfaces.BusinessAuditProducer;
import com.ifood.audit.interfaces.InvalidStateException;
import ifood.teste-oss-java.exceptions.InvalidAuditDataException;
import ifood.teste-oss-java.sample.model.Sample;
import ifood.teste-oss-java.auth.Auth;
import org.springframework.stereotype.Component;

// create your own auditService to centralize call on business audit sdk and publish your audit model
@Component
public class AuditService {

    private static final String TRIBE = "your-tribe";
    private static final String SERVICE_NAME = "teste-oss-java";
    private static final String DOMAIN = "sample";

    private final BusinessAuditProducer businessAuditProducer;

    public AuditService(final BusinessAuditProducer businessAuditProducer) {
        this.businessAuditProducer = businessAuditProducer;
    }

    // for each operation, you can build your audit model
    public void creationAudit(final Sample sample, final AuditHeaders auditHeaders) {
        try {
            // this is a "creation" example that only populate after field with a new model state
            final BusinessAuditPayload<Sample> businessAuditPayload = BusinessAuditPayload
                    .<Sample>builder(AuditUseCase.SAMPLE_CREATION)
                    .withTribe(TRIBE)
                    .withServiceName(SERVICE_NAME)
                    .withDomain(DOMAIN)
                    .withAuthentication(Auth.getUserName())
                    .withAuditHeader(auditHeaders)
                    .withAfterData(sample)
                    .build();

            this.businessAuditProducer.publish(businessAuditPayload);
        } catch (final InvalidStateException ex) {
            // if your model doesn't populate required parameters according to IRC-BAS will be thrown InvalidStateException
            throw new InvalidAuditDataException(AuditUseCase.SAMPLE_CREATION, ex);
        }
    }
}
