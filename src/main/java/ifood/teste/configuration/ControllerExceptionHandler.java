package ifood.teste.configuration;

import ifood.teste.commons.log.ContextLogger;
import ifood.teste.commons.log.ContextLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// A global controller handler
@ControllerAdvice
public class ControllerExceptionHandler {

    private final ContextLogger log = ContextLoggerFactory.getLogger(getClass());

    // Define a ExceptionHandler for Exception to log errors
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        log.error(ex.getCause().toString(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error"));
    }

    // Define a error response structure
    static class ErrorResponse {
        private final int code;
        private final String message;

        public ErrorResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
