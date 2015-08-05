package affableBean;

import java.sql.SQLException;

//import org.h2.server.web.WebServlet;
//import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.embedded.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration.class}) //to disable security until it can be properly set up
@ComponentScan("affableBean.*")	// For subpackage scan
public class AffableBeanApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(AffableBeanApplication.class, args);
        
        /*
        // one method to connect to the H2 console.  if this is used (instead of the bean, below)
        // then you have to Project->Clean before Maven->run as... or else it will not work
    	Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
    	Server server = Server.createTcpServer("-tcpPort" ,"9092", "-tcpAllowOthers") ;    
    	*/
    }
    
/*    
    // this is another way to produce a console to look into the embedded database.
    //but to use this, make sure to change the H2 dependency in POM.XML from runtime to compile
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
    */
}
