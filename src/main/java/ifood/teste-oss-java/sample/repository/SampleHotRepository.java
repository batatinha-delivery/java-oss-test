package ifood.teste-oss-java.sample.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("sampleHotReadRepository")
// Hot Read Repository implementation using read replica database instance
public class SampleHotRepository implements SampleReadRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SampleHotRepository(@Qualifier("roJdbcTemplate") final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public NamedParameterJdbcTemplate jdbcTemplate() {
        return this.jdbcTemplate;
    }
}
