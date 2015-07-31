package affableBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("affableBean")
public class AffableBeanApplication {

    public static void main(String[] args) {
        SpringApplication.run(AffableBeanApplication.class, args);
    }
}
	