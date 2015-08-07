package affableBean.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import affableBean.domain.Category;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.ProductRepository;
import affableBean.repository.RoleRepository;
import affableBean.service.CustomerService;

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
	
	@Autowired
	private RoleRepository roleRepo;
	
//	@Autowired
//	private OrderService orderService;
	
	/**
	 * Auth process
	 */
	@RequestMapping(value = "/login", method= RequestMethod.GET )
	public String loginConsole(@RequestParam(value = "error", required = false) boolean error, ModelMap mm) {
		if(error == true) {
			mm.put("message", "Login Failed!");
		} else {
			mm.put("message", false);
		}
		return "admin/login";
	}
	
	/**
	 * Login succeeded, inside AdminConsole
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String customerConsole(ModelMap mm) {
		mm.put("customerList", customerRepo.findAll());
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
	public String productConsole(ModelMap mm) {
		mm.put("productList", productRepo.findAll());
		return "admin/product";
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String categoryConsole(ModelMap mm) {
		mm.put("categoryList", categoryRepo.findAll());
		return "admin/category";
	}

	@RequestMapping("/productByCategory/{id}")
	public String productByCategory(@PathVariable("id") Integer id, ModelMap mm) {
		mm.put("productList", productRepo.findByCategoryId(id));
		return "admin/product";
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
