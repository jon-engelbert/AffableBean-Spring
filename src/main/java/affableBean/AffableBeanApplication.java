package affableBean;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration.class}) //to disable security until it can be properly set up
@ComponentScan("affableBean.*")	// For subpackage scan
public class AffableBeanApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(AffableBeanApplication.class, args);
        
//    	Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
//    	Server server = Server.createTcpServer("-tcpPort" ,"9092", "-tcpAllowOthers") ;    	
    }
}
	