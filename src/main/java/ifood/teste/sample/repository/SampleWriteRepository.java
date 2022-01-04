package ifood.teste.sample.repository;

import ifood.teste-oss-java.sample.model.Sample;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
// Write Repository implementation using master database instance
public class SampleWriteRepository {

    private static final String INSERT_SQL = "insert into sample (id, name) values (:id,:name);";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SampleWriteRepository(@Qualifier("rwJdbcTemplate") final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Sample persist(final Sample sample) {
        final String id = UUID.randomUUID().toString();
        final Map<String, String> parameters = Map.of(
                "id", id,
                "name", sample.getName());

        final int updated = this.jdbcTemplate.update(INSERT_SQL, parameters);
        if (updated > 0) {
            sample.setId(id);
        }
        
        return sample;
    }

}
