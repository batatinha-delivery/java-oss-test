package ifood.teste.sample.controller;

import ifood.teste-oss-java.commons.context.RequestContext;
import ifood.teste-oss-java.commons.context.RequestContextBuilder;
import ifood.teste-oss-java.commons.log.ContextLogger;
import ifood.teste-oss-java.commons.log.ContextLoggerFactory;
import ifood.teste-oss-java.sample.model.Sample;
import ifood.teste-oss-java.sample.service.SampleService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("samples")
public class SampleController {

    private final ContextLogger log = ContextLoggerFactory.getLogger(getClass());
    private final SampleService sampleService;

    public SampleController(final SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @PostMapping
    public ResponseEntity<Sample> save(@RequestBody final Sample sample, @RequestHeader HttpHeaders headers) {
        // get request information to log and audit
        final RequestContext requestContext = RequestContextBuilder.createFrom(headers);
        log.debug("save request received", requestContext, Map.of("body", sample.toString()));

        // call save service
        final Optional<Sample> saved = this.sampleService.save(sample, requestContext);

        // if saved is empty, return accepted because fallback process will be executed
        if (saved.isEmpty()) {
            log.info("fallback executed and return request accepted. ", requestContext);
            return ResponseEntity.accepted().build();
        }

        // return created when successfully service executed
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.get());
    }

    @GetMapping
    private List<Sample> getAll(@RequestHeader HttpHeaders headers) {
        // debug log with headers information
        log.debug("getAll request received", RequestContextBuilder.createFrom(headers));
        return this.sampleService.findAll();
    }
}