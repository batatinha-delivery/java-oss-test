package ifood.teste.sample.repository;

import ifood.teste-oss-java.sample.model.Sample;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
// Read interface to define contract to Hot and Cold read
public interface SampleReadRepository {

    String QUERY_SQL = "select id, name from sample;";

    NamedParameterJdbcTemplate jdbcTemplate();

    default List<Sample> findAll() {
        final RowMapper<Sample> sampleRowMapper = (resultSet, rowNum) -> new Sample(
                resultSet.getString("id"), resultSet.getString("name")
        );

        return jdbcTemplate().query(QUERY_SQL, sampleRowMapper);
    }
}
