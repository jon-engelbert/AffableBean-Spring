package affableBean.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import affableBean.domain.Customer;
import affableBean.service.CustomerService;

//import AffableBean.Greeting;

@Controller
public class AffablebeanController {
	
	@Autowired
	CustomerService customerService;
	
	 @RequestMapping(value = "/affable", method=RequestMethod.GET)
	 public String index() {
			System.out.println("in controller");
			return "index";
	    }
	 
	 @RequestMapping("/allcust")
	 public @ResponseBody String showAllCustomers() {
		 System.out.println("*******in show all customers");
		 
		 List<Customer> custList = new ArrayList<Customer>();
		 
		 custList = customerService.getAll();
		 System.out.println("*******afer findall");
		 assert (!custList.isEmpty());
		 assert (custList != null);
		 
		 String custStr = new String("");
		 for (Customer custit : custList) {
			 System.out.println("*******in for loop");
			 
			 assert (custit != null);
			 custStr = custit.getName() + " " + custit.getAddress() + " " + custit.getCcNumber() + " ";
			 System.out.println("*******after string assign");
			 
		 }
		 return custStr;
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
