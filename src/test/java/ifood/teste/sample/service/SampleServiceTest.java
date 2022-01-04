package ifood.teste.sample.service;

import com.ifood.audit.interfaces.AuditHeaders;
import com.ifood.audit.interfaces.BusinessAuditPayload;
import com.ifood.audit.interfaces.BusinessAuditProducer;
import com.ifood.featureflags.interfaces.FeatureFlagsResolver;
import ifood.teste.audit.AuditService;
import ifood.teste.audit.AuditUseCase;
import ifood.teste.commons.context.HttpRequestContext;
import ifood.teste.fallback.service.FallbackService;
import ifood.teste.featureflags.FeatureFlag;
import ifood.teste.sample.model.Sample;
import ifood.teste.sample.repository.SampleReadRepository;
import ifood.teste.sample.repository.SampleWriteRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SampleServiceTest {

    @Mock
    private SampleWriteRepository writeRepository;

    @Mock
    private SampleReadRepository hotRepository;

    @Mock
    private SampleReadRepository coldRepository;

    @Spy
    private CircuitBreaker persistenceCircuitBreaker;

    @Spy
    private CircuitBreaker readCircuitBreaker;

    @Mock
    private FallbackService<Sample> fallbackService;

    @Mock
    private FeatureFlagsResolver featureFlagsResolver;

    @Mock
    private BusinessAuditProducer auditProducer;

    @Test
    public void shouldProducerAuditCreationWhenCallSampleSave() {
        final AuditService auditService = new AuditService(auditProducer);
        final SampleService sampleService = new SampleService(writeRepository, hotRepository, coldRepository,
                persistenceCircuitBreaker, readCircuitBreaker, fallbackService, auditService, featureFlagsResolver);

        final Sample sample = new Sample(UUID.randomUUID().toString(), "Sample");
        final String requestId = UUID.randomUUID().toString();
        final String sessionId = UUID.randomUUID().toString();
        final String clientConnection = "127.0.0.1:80";

        final AuditHeaders headers = AuditHeaders.builder()
                .withRequestId(requestId)
                .withSessionId(sessionId)
                .withClientConnection(clientConnection)
                .build();

        Mockito.when(featureFlagsResolver.isActive(FeatureFlag.ACTIVE_ALWAYS_FALLBACK)).thenReturn(false);
        Mockito.when(writeRepository.persist(sample)).thenReturn(sample);

        final ArgumentCaptor<BusinessAuditPayload<Sample>> auditArgument = ArgumentCaptor.forClass(BusinessAuditPayload.class);
        Mockito.doNothing().when(auditProducer).publish(auditArgument.capture());

        sampleService.save(sample, new HttpRequestContext(null, headers));

        Assertions.assertEquals("sample", auditArgument.getValue().getDomain());
        Assertions.assertEquals(AuditUseCase.SAMPLE_CREATION.name(), auditArgument.getValue().getUseCase());
        Assertions.assertEquals(headers.getRequestId(), auditArgument.getValue().getRequestId());
        Assertions.assertEquals(headers.getSessionId(), auditArgument.getValue().getSessionId());
        Assertions.assertEquals(headers.getClientConnection(), auditArgument.getValue().getClientConnection());

        final Sample expectedAfter = auditArgument.getValue().getAfter();
        Assertions.assertEquals(sample.getId(), expectedAfter.getId());
        Assertions.assertEquals(sample.getName(), expectedAfter.getName());

        Assertions.assertNull(auditArgument.getValue().getBefore());
        Assertions.assertNotNull(auditArgument.getValue().getTimestamp());
    }

    @Test
    public void shouldFallbackWhenRepositoryFail() {
        final AuditService auditService = new AuditService(auditProducer);
        final SampleService sampleService = new SampleService(writeRepository, hotRepository, coldRepository,
                persistenceCircuitBreaker, readCircuitBreaker, fallbackService, auditService, featureFlagsResolver);

        final Sample sample = new Sample(UUID.randomUUID().toString(), "Sample");

        Mockito.when(featureFlagsResolver.isActive(FeatureFlag.ACTIVE_ALWAYS_FALLBACK)).thenReturn(false);
        Mockito.when(writeRepository.persist(sample)).thenThrow(new RuntimeException());
        Mockito.doNothing().when(fallbackService).publish(sample);

        sampleService.save(sample, null);
        Mockito.verify(fallbackService, Mockito.times(1)).publish(sample);
    }

    @Test
    public void shouldFallbackWhenAlwaysFallbackActive() {
        final AuditService auditService = new AuditService(auditProducer);
        final SampleService sampleService = new SampleService(writeRepository, hotRepository, coldRepository,
                persistenceCircuitBreaker, readCircuitBreaker, fallbackService, auditService, featureFlagsResolver);

        final Sample sample = new Sample(UUID.randomUUID().toString(), "Sample");

        Mockito.when(featureFlagsResolver.isActive(FeatureFlag.ACTIVE_ALWAYS_FALLBACK)).thenReturn(true);
        Mockito.doNothing().when(fallbackService).publish(sample);

        sampleService.save(sample, null);
        Mockito.verify(fallbackService, Mockito.times(1)).publish(sample);
    }

    @Test
    public void shouldReturnHotDataWhenHaveNoErrors() {
        final AuditService auditService = new AuditService(auditProducer);
        final SampleService sampleService = new SampleService(writeRepository, hotRepository, coldRepository,
                persistenceCircuitBreaker, readCircuitBreaker, fallbackService, auditService, featureFlagsResolver);

        final List<Sample> data = Arrays.asList(new Sample(), new Sample());
        Mockito.when(hotRepository.findAll()).thenReturn(data);

        final List<Sample> resultList = sampleService.findAll();
        Assertions.assertEquals(data.size(), resultList.size());

        Mockito.verify(coldRepository, Mockito.never()).findAll();
    }

    @Test
    public void shouldReturnColdDataWhenHaveErrors() {
        final AuditService auditService = new AuditService(auditProducer);
        final SampleService sampleService = new SampleService(writeRepository, hotRepository, coldRepository,
                persistenceCircuitBreaker, readCircuitBreaker, fallbackService, auditService, featureFlagsResolver);

        final List<Sample> data = Arrays.asList(new Sample(), new Sample());
        Mockito.when(hotRepository.findAll()).thenThrow(new RuntimeException("read database fail"));
        Mockito.when(coldRepository.findAll()).thenReturn(data);

        final List<Sample> resultList = sampleService.findAll();
        Assertions.assertEquals(data.size(), resultList.size());
    }
}