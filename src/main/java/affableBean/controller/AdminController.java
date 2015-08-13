package affableBean.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import affableBean.cart.Cart;
import affableBean.domain.Category;
import affableBean.domain.Customer;
import affableBean.domain.CustomerOrder;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.ProductRepository;
import affableBean.service.OrderService;
import affableBean.service.ValidatorService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired 
	private CustomerRepository customerRepo;
	
	@Autowired 
	private ProductRepository productRepo;
	
	@Autowired 
	private CategoryRepository categoryRepo;
	
//	@Autowired
//	private CustomerService customerService;
	
	@Autowired 
	private CustomerOrderRepository orderRepo;
	
//	@Autowired
//	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepo;

//	@Autowired
//	private RoleRepository roleRepo;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ValidatorService validator;
	
	@Autowired
	DataSource datasource;
	
	/**
	 * Auth process
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST } )
	public String loginConsole(@RequestParam(value = "error", required = false) String error, ModelMap mm) {

		if(error != null) {
			mm.put("message", "Login Failed!");
		} else {
			mm.put("message", false);
		}
		return "admin/login";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutConsole(HttpSession session) {

		if(session!=null)
			session.invalidate();
		return "front_store/home";
	}


	/**
	 * Login succeeded, inside AdminConsole
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String customerConsole(ModelMap mm) {
		mm.put("customerList", customerRepo.findAll());
		return "admin/index";
	}
	
	@RequestMapping(value = "/viewCustomers", method = RequestMethod.GET)
	public String viewCustomers(ModelMap mm) {
		mm.put("customerList", customerRepo.findAll());
		return "admin/index";
	}
	
	@RequestMapping("/viewOrders")
	public String viewOrders(ModelMap mm) {
		
		mm.put("orderList", orderRepo.findAll());
		return "admin/index";
	}
	
	@RequestMapping(value = "/customerRecord", method = RequestMethod.GET)
    public String getCustomerRecord(@RequestParam("id") Integer id, ModelMap mm) {

		// get customer details
		Customer customer = customerRepo.findById(id);
        mm.put("customerRecord", customer);

        // get customer order details
        CustomerOrder order = orderRepo.findByCustomer(customer);
        mm.put("order", order);
        
        return "admin/index";
    }
	
	@RequestMapping(value = "/orderRecord", method = RequestMethod.GET)
	public String getOrderRecord(@RequestParam("id") Integer id, ModelMap mm) {

        // get order details
        Map<String, Object> orderMap = orderService.getOrderDetails(id);

        // place order details in request scope
        mm.put("customer", orderMap.get("customer"));
        mm.put("products", orderMap.get("products"));
        mm.put("orderRecord", orderMap.get("orderRecord"));
        mm.put("orderedProducts", orderMap.get("orderedProducts"));
        mm.put("deliverySurcharge", Cart._deliverySurcharge);
        
        return "admin/index";
    }
	
	@RequestMapping(value = "/customerEdit", method = RequestMethod.GET)
	public String editCustomer(@RequestParam("id") Integer id, ModelMap mm) {

		Customer customer = new Customer();
		if (id != null) {
			customer = customerRepo.findById(id);
		}

        // place customer details in request scope
        mm.put("customer", customer);
        
        return "admin/customer";
    }
	
	@RequestMapping(value = "/customerEdit", method = RequestMethod.POST)
	public String editCustomerPost(final Customer customer, final BindingResult bindingResult, ModelMap mm, HttpServletRequest request) {
		
		System.out.println("in customerEdit post");
		Integer id = 0;
		if (customer != null) {
			id = customer.getId();
			System.out.println("id: " + customer.getId() + " name: " + customer.getName());
		}
		
		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult error");
			mm.put("error", true);
            return "admin/customer"; 
		}
		
		// validate customer data
        boolean validationErrorFlag = false;
        validationErrorFlag = validator.validateCustomer(customer, request);
        
        if (validationErrorFlag == true) {
            mm.put("validationErrorFlag", validationErrorFlag);
            return "admin/customer";

        // otherwise, update customer to database
        } else {
        	Customer updatedCustomer = customerRepo.save(customer);
        	mm.put("success", true);
        	mm.put("customer", updatedCustomer);
        }
        
        return "admin/customer";
    }
	
	@RequestMapping(value = "/member", method = RequestMethod.GET)
	public String memberConsole(ModelMap mm) {
		mm.put("memberList", memberRepo.findAll());
		return "admin/member";
	}
	
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String orderConsole(ModelMap mm) {
		mm.put("orderList", orderRepo.findAll());
		return "admin/order";
	}
	
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public String productConsole(@RequestParam(value="id", required=false) Integer id, ModelMap mm) {
		if (id != null) {
			mm.put("productList", productRepo.findByCategoryId(id));
		} else {
			mm.put("productList", productRepo.findAll());
		}
		return "admin/product";
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String categoryConsole(ModelMap mm) {
		mm.put("categoryList", categoryRepo.findAll());
		return "admin/category";
	}

	@RequestMapping(value = "/category/add", method = RequestMethod.POST)
	public String addCategory(@ModelAttribute Category cat, ModelMap mm) {
		categoryRepo.save(cat);
		return "redirect:/admin/category";
	}
	
	@RequestMapping(value = "/category/edit/{id}", method = RequestMethod.GET)
	public String categoryEdit(@PathVariable("id") Integer id, ModelMap mm) {
		Category selectedCategory = categoryRepo.findById(id);
		mm.put("category", selectedCategory);
		return "admin/editcategory";
	}

	@RequestMapping(value = "/category/delete/{id}", method = RequestMethod.GET)
	public String categoryDelete(@PathVariable("id") Integer id, ModelMap mm) {
		categoryRepo.delete(id);
		return "redirect:/admin/category";
	}

}
