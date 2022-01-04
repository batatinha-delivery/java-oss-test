package ifood.teste.sample.service;

import com.ifood.featureflags.interfaces.FeatureFlagsResolver;
import ifood.teste-oss-java.audit.AuditService;
import ifood.teste-oss-java.commons.context.RequestContext;
import ifood.teste-oss-java.commons.log.ContextLogger;
import ifood.teste-oss-java.commons.log.ContextLoggerFactory;
import ifood.teste-oss-java.fallback.service.FallbackService;
import ifood.teste-oss-java.featureflags.FeatureFlag;
import ifood.teste-oss-java.sample.model.Sample;
import ifood.teste-oss-java.sample.repository.SampleReadRepository;
import ifood.teste-oss-java.sample.repository.SampleWriteRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SampleService {

    private final ContextLogger log = ContextLoggerFactory.getLogger(getClass());

    private final SampleWriteRepository writeRepository;
    private final SampleReadRepository hotReadRepository;
    private final SampleReadRepository coldReadRepository;
    private final CircuitBreaker persistenceCircuitBreaker;
    private final CircuitBreaker readCircuitBreaker;
    private final FallbackService<Sample> fallbackService;
    private final AuditService auditService;
    private final FeatureFlagsResolver featureFlagsResolver;

    public SampleService(
            final SampleWriteRepository writeRepository,
            @Qualifier("sampleHotReadRepository") final SampleReadRepository hotReadRepository,
            @Qualifier("sampleColdReadRepository") final SampleReadRepository coldReadRepository,
            final CircuitBreaker persistenceCircuitBreaker,
            final CircuitBreaker readCircuitBreaker,
            final FallbackService<Sample> fallbackService,
            final AuditService auditService,
            final FeatureFlagsResolver featureFlagsResolver) {
        this.writeRepository = writeRepository;
        this.hotReadRepository = hotReadRepository;
        this.coldReadRepository = coldReadRepository;
        this.persistenceCircuitBreaker = persistenceCircuitBreaker;
        this.readCircuitBreaker = readCircuitBreaker;
        this.fallbackService = fallbackService;
        this.featureFlagsResolver = featureFlagsResolver;
        this.auditService = auditService;
    }

    public Optional<Sample> save(final Sample sample, final RequestContext requestContext) {
        // check if flag is enable
        if (this.featureFlagsResolver.isActive(FeatureFlag.ACTIVE_ALWAYS_FALLBACK)) {
            // if fallback is active log and call fallback method
            log.warn("[FALLBACK] Fallback is always active. executing fallback", requestContext, Map.of("sample", sample.toString()));

            // call fallback method
            this.fallbackService.publish(sample);

            // returning empty
            return Optional.empty();
        }

        // try to persist sample
        final Optional<Sample> sampleSaved = Try.ofSupplier(CircuitBreaker.decorateSupplier(this.persistenceCircuitBreaker, () -> {
                    // call repository layer to persist in database
                    final Sample persisted = this.writeRepository.persist(sample);

                    // log succeed
                    log.info("persisted sample", requestContext,
                            Map.of("id", sample.getId(), "sample", sample.toString()));

                    // returning persisted sample
                    return Optional.of(persisted);
                }))
                // recover in case o failure
                .recover(throwable -> {
                    // warning failure on persistence and signals the fallback
                    log.warn("[FALLBACK] Failure to persist. Publish to fallback",
                            requestContext, Map.of("sample", sample.toString()), throwable);

                    // call fallback method
                    this.fallbackService.publish(sample);

                    // returning empty
                    return Optional.empty();
                }).get();

        // if sample persisted call audit
        sampleSaved.ifPresent(value -> this.auditService.creationAudit(value, requestContext.getAuditHeaders()));

        // return persisted sample for success or null for fallback
        return sampleSaved;
    }

    public List<Sample> findAll() {
        // try to read sample from hot database
        return Try.ofSupplier(CircuitBreaker.decorateSupplier(this.readCircuitBreaker,
                        // read from hot database
                        this.hotReadRepository::findAll))

                // recover in case o failure
                .recover(throwable -> {
                    // warning failure on read replica database
                    log.warn("[FALLBACK] Failure to getAll rom read replica database.", throwable);

                    // read from cold database (disaster recovery database)
                    return this.coldReadRepository.findAll();
                }).get();
    }
}