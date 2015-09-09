package affableBean;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;

public class ConfigurationInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String DEFAULT_PROPS = "classpath*:/*-defaults.properties";
    private static final String EXTERNAL_PROPS = "file:/my-config-path/*.properties";

    private static final Logger logger = LogManager
			.getLogger(ConfigurationInitializer.class);

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		logger.info("About to setActiveProfiles(dev)");
		applicationContext.getEnvironment().setActiveProfiles("dev");
//		ctx.load("*Context.xml");
//		applicationContext.refresh();
//	    Resource[] defaultConfigs = null;
//	    Resource[] externalConfigs = null;
//		try {
//			defaultConfigs = applicationContext.getResources(DEFAULT_PROPS);
//	        externalConfigs = applicationContext.getResources(EXTERNAL_PROPS);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
		final ConfigurableEnvironment env = applicationContext.getEnvironment();
		final MutablePropertySources mps = env.getPropertySources();
		//	        for (Resource r : externalConfigs) {
//	            try {
//					mps.addLast(new ResourcePropertySource(r.getFilename(), r));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	        }
//	        for (Resource r : defaultConfigs) {
//	            try {
//					mps.addLast(new ResourcePropertySource(r.getFilename(), r));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	        }   
	    }
	// } else {
	// logger.info("Application running local");
	// applicationContext.getEnvironment().setActiveProfiles("dev");
	// }

}
