package affableBean.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import affableBean.domain.Category;
import affableBean.domain.Customer;
import affableBean.domain.Product;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.ProductRepository;

//import AffableBean.Greeting;

@Controller
public class AffablebeanController {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private MemberRepository memberRepo;

	@RequestMapping(value = "/affable", method = RequestMethod.GET)
	public String index(ModelMap mm) {
		mm.put("categoryList", categoryRepo.findAll());
		return "index";
	}

	@RequestMapping("/allcust")
	public @ResponseBody String showAllCustomers() {
		List<Customer> custList = new ArrayList<Customer>();
		custList = customerRepo.findAll();
		String custStr;
		ArrayList<String> custStrList = new ArrayList<>();
		for (Customer cust : custList) {
			custStr = new String("<p>" + cust.getName() + " "
					+ cust.getAddress() + " " + cust.getCcNumber() + "</p>");
			custStrList.add(custStr);
		}
		return String.join(", ", custStrList);
	}

	@RequestMapping("/allcategories")
	public @ResponseBody String showAllCategories() {
		List<Category> catList = new ArrayList<Category>();
		catList = categoryRepo.findAll();
		String catStr;
		ArrayList<String> catStrList = new ArrayList<>();
		for (Category cat : catList) {
			catStr = new String("<p>" + cat.getName() + "</p>");
			catStrList.add(catStr);
		}
		return String.join(", ", catStrList);
	}

	@RequestMapping("/allproducts")
	public @ResponseBody String showAllProducts() {
		List<Product> prodList = new ArrayList<Product>();
		prodList = productRepo.findAll();
		String prodStr;
		ArrayList<String> prodStrList = new ArrayList<>();
		for (Product prod : prodList) {
			prodStr = new String("<p>" + prod.getName() + ". Category: "
					+ prod.getCategory().getName() + "</p>");
			prodStrList.add(prodStr);

		}
		return String.join(", ", prodStrList);
	}

	@RequestMapping("/showdb")
	public @ResponseBody String fetchTables() {
		String retstr = new String();
		StringBuilder sb = new StringBuilder();

		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:~/test",
					"sa", "");

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
