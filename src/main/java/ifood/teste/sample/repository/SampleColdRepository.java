package ifood.teste.sample.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("sampleColdReadRepository")
// Cold Read Repository implementation using disaster recovery database instance
public class SampleColdRepository implements SampleReadRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SampleColdRepository(@Qualifier("drJdbcTemplate") final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public NamedParameterJdbcTemplate jdbcTemplate() {
        return this.jdbcTemplate;
    }
}
