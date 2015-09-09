package affableBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DataSourceConfig {
	
    @Value("${spring.datasource.driverClassName}")
    private String databaseDriverClassName;
 
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
 
    @Value("${spring.datasource.username}")
    private String databaseUsername;
 
    @Value("${spring.datasource.password}")
    private String databasePassword;
    
	@Bean
	@Profile("prod")
	public DataSource mySqlDataSource() throws NamingException {
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setDriverClassName(databaseDriverClassName);
        ds.setUrl(datasourceUrl);
        ds.setUsername(databaseUsername);
        ds.setPassword(databasePassword);
 
        return ds;
    }

//	@Bean(name="dataSource")
//	@Profile("prod")
//	public DataSource getProdDataSource() throws NamingException {
//        Context ctx = new InitialContext();
//        // TODO: set the path to the production database
//        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
//	  }	
    @Bean
	@Profile("dev")
	public DataSource embeddedDataSource() {
//		return new DevDatabaseUtil();
        return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.HSQL)
        .addScript("src/main/resources/schema.sql")
        .addScript("src/main/resources/data.sql")
        .build();
	  }	
	
}