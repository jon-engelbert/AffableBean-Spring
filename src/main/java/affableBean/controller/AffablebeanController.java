package affableBean.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//import AffableBean.Greeting;

@Controller
public class AffablebeanController {
	
	
	 @RequestMapping(value = "/affable", method=RequestMethod.GET)
	 public String index() {
			System.out.println("in controller");
			return "index";
	    }


}
