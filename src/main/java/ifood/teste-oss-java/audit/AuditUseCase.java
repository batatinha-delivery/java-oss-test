package ifood.teste-oss-java.audit;

import com.ifood.audit.interfaces.BusinessAuditPayload;

// Define yours audit use cases
public enum AuditUseCase implements BusinessAuditPayload.UseCase {
    SAMPLE_CREATION
    ;

    // value published in useCase field, by default, is enum name
    @Override
    public String useCaseName() {
        return this.name();
    }
}