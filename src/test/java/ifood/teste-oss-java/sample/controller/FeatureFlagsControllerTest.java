package ifood.teste-oss-java.sample.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


@SpringBootTest(
        properties = {
                "business.audit.disable=true",
                // override fileLocation only to test
                "feature-flags.fileLocation=./src/test/resources/ft.properties"
        }
)
@AutoConfigureMockMvc
class FeatureFlagsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnMappedFeatureFlagsWhenCallGetEndpoint() throws Exception {
        final byte[] encoded = Files.readAllBytes(Paths.get("src", "test", "resources", "expected-feature-flags.json"));
        final String expected = new String(encoded, StandardCharsets.UTF_8);

        mvc.perform(MockMvcRequestBuilders.get("/feature-flags"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(expected));
    }

}