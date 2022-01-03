package ifood.teste-oss-java.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// Jdbc configurations
@Configuration
@ConfigurationProperties(prefix = "jdbc")
public class JdbcProperties {

    private String username;
    private String password;
    private String InitSql;
    private JdbcDataSourceProperties rw; // Master database properties
    private JdbcDataSourceProperties ro; //  Read replica database properties
    private JdbcDataSourceProperties dr; // Disaster recovery database properties

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitSql() {
        return InitSql;
    }

    public void setInitSql(String initSql) {
        InitSql = initSql;
    }

    public JdbcDataSourceProperties getRw() {
        return rw;
    }

    public void setRw(JdbcDataSourceProperties rw) {
        this.rw = rw;
    }

    public JdbcDataSourceProperties getRo() {
        return ro;
    }

    public void setRo(JdbcDataSourceProperties ro) {
        this.ro = ro;
    }

    public JdbcDataSourceProperties getDr() {
        return dr;
    }

    public void setDr(JdbcDataSourceProperties dr) {
        this.dr = dr;
    }

    public static class JdbcDataSourceProperties {
        private String url;
        private Integer maxPoolSize;
        private Long maxLifetimeInMinutes;
        private Long leakDetectionThresholdInMilliseconds;
        private Long connectionTimeoutInMilliseconds;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(Integer maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public Long getMaxLifetimeInMinutes() {
            return maxLifetimeInMinutes;
        }

        public void setMaxLifetimeInMinutes(Long maxLifetimeInMinutes) {
            this.maxLifetimeInMinutes = maxLifetimeInMinutes;
        }

        public Long getLeakDetectionThresholdInMilliseconds() {
            return leakDetectionThresholdInMilliseconds;
        }

        public void setLeakDetectionThresholdInMilliseconds(Long leakDetectionThresholdInMilliseconds) {
            this.leakDetectionThresholdInMilliseconds = leakDetectionThresholdInMilliseconds;
        }

        public Long getConnectionTimeoutInMilliseconds() {
            return connectionTimeoutInMilliseconds;
        }

        public void setConnectionTimeoutInMilliseconds(Long connectionTimeoutInMilliseconds) {
            this.connectionTimeoutInMilliseconds = connectionTimeoutInMilliseconds;
        }
    }
}
