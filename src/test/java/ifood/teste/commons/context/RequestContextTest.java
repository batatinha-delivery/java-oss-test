package ifood.teste.commons.context;

import com.ifood.audit.interfaces.AuditHeaders;
import ifood.teste.commons.context.HttpRequestContext.IFoodHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.Map;

class RequestContextTest {

    @Test
    public void shouldCreateRequestAttributesWhenReceivedHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(IFoodHeaders.IFOOD_REQUEST_ID.getHeaderName(), "42bbae4e-9cc1-469c-a033-f8f1b0d722cb");
        headers.add(IFoodHeaders.IFOOD_SESSION_ID.getHeaderName(), "e3098023-bd4b-49a2-b787-6a07c45bd2de");
        headers.add(IFoodHeaders.CLIENT_CONNECTION.getHeaderName(), "127.0.0.1:80");
        headers.add(IFoodHeaders.ACCOUNT_ID.getHeaderName(), "16e3fd9f-5845-4ea0-8529-f8ed6b7b4528");
        headers.add(IFoodHeaders.MERCHANT_ID.getHeaderName(), "559be040-f84d-4ebb-ad10-5abd45a53171");
        headers.add(IFoodHeaders.DRIVER_ID.getHeaderName(), "5b8f298c-1fb1-4900-bf7c-e1d4c303e5a3");
        headers.add(IFoodHeaders.ORDER_ID.getHeaderName(), "51e5c617-991b-4ba0-a63a-ff0ef712ae8c");

        final HttpRequestContext requestContext = RequestContextBuilder.createFrom(headers);
        Assertions.assertEquals("16e3fd9f-5845-4ea0-8529-f8ed6b7b4528", requestContext.getValue(IFoodHeaders.ACCOUNT_ID));
        Assertions.assertEquals("559be040-f84d-4ebb-ad10-5abd45a53171", requestContext.getValue(IFoodHeaders.MERCHANT_ID));
        Assertions.assertEquals("5b8f298c-1fb1-4900-bf7c-e1d4c303e5a3", requestContext.getValue(IFoodHeaders.DRIVER_ID));
        Assertions.assertEquals("51e5c617-991b-4ba0-a63a-ff0ef712ae8c", requestContext.getValue(IFoodHeaders.ORDER_ID));

        final AuditHeaders auditHeaders = requestContext.getAuditHeaders();
        Assertions.assertNotNull(auditHeaders);
        Assertions.assertEquals("42bbae4e-9cc1-469c-a033-f8f1b0d722cb", auditHeaders.getRequestId());
        Assertions.assertEquals("e3098023-bd4b-49a2-b787-6a07c45bd2de", auditHeaders.getSessionId());
        Assertions.assertEquals("127.0.0.1:80", auditHeaders.getClientConnection());
    }

    @Test
    public void shouldShareRequestContextParametersButNotSharedContextValueWhenGetValuesWithContextValue() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(IFoodHeaders.IFOOD_REQUEST_ID.getHeaderName(), "16e3fd9f-5845-4ea0-8529-f8ed6b7b4528");
        final HttpRequestContext requestContext = RequestContextBuilder.createFrom(headers);

        final Map<String, Object> requestContextWithDomain = requestContext.getValuesWithContextValue(Map.of("domain", "unit test"));
        Assertions.assertEquals(2, requestContextWithDomain.size());
        Assertions.assertEquals("16e3fd9f-5845-4ea0-8529-f8ed6b7b4528", requestContextWithDomain.get(IFoodHeaders.IFOOD_REQUEST_ID.getProperty()));
        Assertions.assertEquals("{domain=unit test}", requestContextWithDomain.get("context").toString());

        final Map<String, Object> requestContextWithUseCase = requestContext.getValuesWithContextValue(Map.of("useCase", "context validation"));
        Assertions.assertEquals(2, requestContextWithDomain.size());
        Assertions.assertEquals("16e3fd9f-5845-4ea0-8529-f8ed6b7b4528", requestContextWithUseCase.get(IFoodHeaders.IFOOD_REQUEST_ID.getProperty()));
        Assertions.assertEquals("{useCase=context validation}", requestContextWithUseCase.get("context").toString());
    }
}