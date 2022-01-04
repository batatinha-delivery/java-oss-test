package ifood.teste.configuration;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

// Master (rw), Read Replica (ro) and Disaster Recovery (dr) databaseSource configurations
@Configuration
public class JdbcConfiguration {

    private final JdbcProperties jdbcProperties;
    private final MeterRegistry meterRegistry;

    public JdbcConfiguration(
            final JdbcProperties jdbcProperties,
            final MeterRegistry meterRegistry) {
        this.jdbcProperties = jdbcProperties;
        this.meterRegistry = meterRegistry;
    }

    // Master database instance (rw)
    @Bean
    @Primary
    @Qualifier("rwDataSource")
    public DataSource rwDataSource() {
        return createDataSource(this.jdbcProperties.getRw());
    }

    // Read replica database instance (ro)
    @Bean
    @Qualifier("roDataSource")
    public DataSource roDataSource() {
        return createDataSource(this.jdbcProperties.getRo());
    }

    // Disaster recovery database instance (dr)
    @Bean
    @Qualifier("drDataSource")
    public DataSource drDataSource() {
        return createDataSource(this.jdbcProperties.getDr());
    }

    // NamedParameter from JdbcTemplate to write in master instance
    @Bean
    @Primary
    @Qualifier("rwDataSource")
    public NamedParameterJdbcTemplate rwJdbcTemplate(@Qualifier("rwDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    // NamedParameter from JdbcTemplate to read in read replica instance
    @Bean
    @Qualifier("roJdbcTemplate")
    public NamedParameterJdbcTemplate roJdbcTemplate(@Qualifier("roDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    // NamedParameter from JdbcTemplate to read in disaster recovery instance
    @Bean
    @Qualifier("drJdbcTemplate")
    public NamedParameterJdbcTemplate drJdbcTemplate(@Qualifier("drDataSource") DataSource dataSource)  {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    private HikariDataSource createDataSource(final JdbcProperties.JdbcDataSourceProperties dataSourceProperties) {
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setJdbcUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(this.jdbcProperties.getUsername());
        dataSource.setPassword(this.jdbcProperties.getPassword());
        dataSource.setMaximumPoolSize(dataSourceProperties.getMaxPoolSize());
        dataSource.setMaxLifetime(dataSourceProperties.getMaxLifetimeInMinutes());
        dataSource.setLeakDetectionThreshold(dataSourceProperties.getLeakDetectionThresholdInMilliseconds());
        dataSource.setConnectionTimeout(dataSourceProperties.getConnectionTimeoutInMilliseconds());
        dataSource.setConnectionInitSql(this.jdbcProperties.getInitSql());
        dataSource.setMetricsTrackerFactory(new MicrometerMetricsTrackerFactory(this.meterRegistry));
        return dataSource;
    }

}
