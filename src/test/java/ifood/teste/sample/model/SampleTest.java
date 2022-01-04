package ifood.teste-oss-java.sample.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SampleTest {

    @Test
    public void shouldReturnNameWhenNameIsSingleName() {
        final Sample sample = new Sample("0b3b589f-bd3a-494e-9d8d-798e40dc8e0b", "Name");
        Assertions.assertEquals("Sample{id='0b3b589f-bd3a-494e-9d8d-798e40dc8e0b', name='Name'}", sample.toString());
    }

    @Test
    public void shouldTruncateNameWhenNameHasLastName() {
        final Sample sample = new Sample("9a13e721-4af2-4219-b06d-f9e8cfc67c7b", "firstName lastName");
        Assertions.assertEquals("Sample{id='9a13e721-4af2-4219-b06d-f9e8cfc67c7b', name='firstName'}", sample.toString());
    }

    @Test
    public void shouldTruncateNameWhenHasMoreSurnames() {
        final Sample sample = new Sample("84ce9539-a754-4760-952e-a9a34e6c4721", "firstName and lastName");
        Assertions.assertEquals("Sample{id='84ce9539-a754-4760-952e-a9a34e6c4721', name='firstName'}", sample.toString());
    }

    @Test
    public void shouldReturnEmptyWhenNameIsNull() {
        final Sample sample = new Sample("45aa4417-9487-4593-8bc2-47a8003ab48e", null);
        Assertions.assertEquals("Sample{id='45aa4417-9487-4593-8bc2-47a8003ab48e', name=''}", sample.toString());
    }
}