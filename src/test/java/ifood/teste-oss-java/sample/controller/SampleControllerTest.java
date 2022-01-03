package ifood.teste-oss-java.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifood.audit.interfaces.AuditHeaders;
import ifood.teste-oss-java.commons.context.RequestContext;
import ifood.teste-oss-java.configuration.ControllerExceptionHandler;
import ifood.teste-oss-java.sample.model.Sample;
import ifood.teste-oss-java.sample.service.SampleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(controllers = SampleController.class)
@Import(ControllerExceptionHandler.class)
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SampleService service;


    @Test
    public void shouldReturnCreatedWhenPersistSample() throws Exception {
        final Sample sample = new Sample(UUID.randomUUID().toString(), "Sample");

        final ArgumentCaptor<Sample> sampleArgumentCaptor = ArgumentCaptor.forClass(Sample.class);
        Mockito.when(service.save(sampleArgumentCaptor.capture(), Mockito.any(RequestContext.class))).thenReturn(Optional.of(sample));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(sample)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string(this.objectMapper.writeValueAsString(sample)));

        Assertions.assertEquals(sample.getName(), sampleArgumentCaptor.getValue().getName());
    }

    @Test
    public void shouldReturnAcceptedWhenNotPersistSample() throws Exception {
        final Sample sample = new Sample(UUID.randomUUID().toString(), "Sample");

        Mockito.when(service.save(Mockito.any(Sample.class), Mockito.any(RequestContext.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(sample)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    public void shouldGetHeadersToAuditWhenProcessRequest() throws Exception {
        final Sample sample = new Sample(UUID.randomUUID().toString(), "Sample");
        final String requestId = UUID.randomUUID().toString();
        final String sessionId = UUID.randomUUID().toString();
        final String clientConnection = "127.0.0.1:80";

        final AuditHeaders headers = AuditHeaders.builder()
                .withRequestId(requestId)
                .withSessionId(sessionId)
                .withClientConnection(clientConnection)
                .build();

        final ArgumentCaptor<RequestContext> auditArgument = ArgumentCaptor.forClass(RequestContext.class);
        Mockito.when(service.save(Mockito.any(Sample.class), auditArgument.capture())).thenReturn(Optional.of(sample));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AuditHeaders.REQUEST_ID_HEADER, requestId)
                        .header(AuditHeaders.SESSION_ID_HEADER, sessionId)
                        .header(AuditHeaders.CLIENT_CONNECTION_HEADER, clientConnection)

                        .header("X-iFood-Account-Id", UUID.randomUUID().toString())
                        .header("X-iFood-Order-Id", UUID.randomUUID().toString())

                        .content(this.objectMapper.writeValueAsString(sample)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string(this.objectMapper.writeValueAsString(sample)));

        final RequestContext requestContext = auditArgument.getValue();
        Assertions.assertEquals(headers.getRequestId(), requestContext.getAuditHeaders().getRequestId());
        Assertions.assertEquals(headers.getSessionId(), requestContext.getAuditHeaders().getSessionId());
        Assertions.assertEquals(headers.getClientConnection(), requestContext.getAuditHeaders().getClientConnection());
    }
}