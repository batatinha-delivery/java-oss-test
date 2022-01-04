package ifood.teste.exceptions;

import com.ifood.audit.interfaces.BusinessAuditPayload;
import com.ifood.audit.interfaces.InvalidStateException;

public class InvalidAuditDataException extends RuntimeException {

    public InvalidAuditDataException(final BusinessAuditPayload.UseCase useCase, final InvalidStateException ex) {
        super("invalid data to audit use case:  " + useCase.useCaseName(), ex);
    }
}
