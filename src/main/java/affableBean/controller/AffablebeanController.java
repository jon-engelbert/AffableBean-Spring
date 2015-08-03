package affableBean.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import affableBean.Greeting;

@Controller
@RequestMapping("/affable")
public class AffablebeanController {
	
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
	
	 @RequestMapping(method=RequestMethod.GET)
	 public @ResponseBody String sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
			System.out.println("in controller");
//	        return new Greeting(counter.incrementAndGet(), String.format(template, name));
			return "Hello from affableBean";
	    }


}
