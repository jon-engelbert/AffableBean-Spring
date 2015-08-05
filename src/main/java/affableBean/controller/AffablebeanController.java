package affableBean.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//import AffableBean.Greeting;

@Controller
public class AffablebeanController {
	
	
	 @RequestMapping(value = "/affable", method=RequestMethod.GET)
	 public String index() {
			System.out.println("in controller");
			return "index";
	    }
	 
	 @RequestMapping("/showdb")
	 public @ResponseBody String fetchTables() {
		 String retstr = new String();
		 StringBuilder sb = new StringBuilder();
		 
		 try {
			 Class.forName("org.h2.Driver");
		     Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");

			 DatabaseMetaData md = conn.getMetaData();
			 ResultSet rs = md.getTables(null, null, "%", null);
			 while (rs.next()) {
				 sb.append(" " + rs.getString(3));
			 }
		 } catch (Exception e) {
			 System.out.println(e.getMessage());
		 }
		 
		 retstr = sb.toString();
		 
		 System.out.println(retstr);
		 
		 return retstr;

	 }



}
