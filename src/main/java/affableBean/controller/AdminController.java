package affableBean.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import affableBean.cart.Cart;
import affableBean.domain.Category;
import affableBean.domain.Customer;
import affableBean.domain.CustomerOrder;
import affableBean.domain.Product;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.ProductRepository;
import affableBean.service.CustomerDto;
import affableBean.service.CustomerDtoService;
import affableBean.service.CustomerService;
import affableBean.service.OrderService;
import affableBean.service.ProductDto;
import affableBean.service.ProductDtoService;
import affableBean.service.ValidatorService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private CustomerDtoService customerDtoService;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private ProductDtoService productDtoService;

	@Autowired
	private CategoryRepository categoryRepo;

	 @Autowired
	 private CustomerService customerService;

	@Autowired
	private CustomerOrderRepository orderRepo;

	// @Autowired
	// private MemberService memberService;

	@Autowired
	private MemberRepository memberRepo;

	// @Autowired
	// private RoleRepository roleRepo;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ValidatorService validator;

	@Autowired
	DataSource datasource;

	/**
	 * Auth process
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String loginConsole(
			@RequestParam(value = "error", required = false) String error,
			ModelMap mm) {

		if (error != null) {
			mm.put("message", "Login Failed!");
		} else {
			mm.put("message", false);
		}
		return "admin/login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutConsole(HttpSession session) {

		if (session != null)
			session.invalidate();
		return "redirect:/home";
	}

	/**
	 * Login succeeded, inside AdminConsole
	 */
	@RequestMapping(value = {"", "/viewCustomers"}, method = RequestMethod.GET)
	public String customerConsole(@RequestParam(value="page", required=false, defaultValue="1") Integer pageNumber, ModelMap mm) {
		Page<Customer> page = customerService.findAllCustomers(pageNumber);
		
		int current = page.getNumber() + 1;
	    int begin = Math.max(1, current - 5);
	    int end = Math.min(begin + 10, page.getTotalPages());
		
	    mm.put("customerList", page.getContent());
	    mm.put("customerPage", page);
	    mm.put("beginIndex", begin);
	    mm.put("endIndex", end);
	    mm.put("currentIndex", current);
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
		List<CustomerOrder> orders = orderRepo.findByCustomer(customer);
		System.out
				.println("Number of orders: " + String.valueOf(orders.size()));
		if (orders.size() == 1)
			mm.put("order", orders.get(0));
		else
			mm.put("orders", orders);

		return "admin/index";
	}

	@RequestMapping(value = "/orderRecord", method = RequestMethod.GET)
	public String getOrderRecord(@RequestParam("id") Integer id, ModelMap mm) {
		System.out.println("id");
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

	@RequestMapping(value = "/viewCustomerOrders", method = RequestMethod.GET)
	public String getCustomerOrders(@RequestParam("id") Integer id, ModelMap mm) {

		Customer customer = customerRepo.findById(id);
		List<CustomerOrder> orders = orderRepo.findByCustomer(customer);
		mm.put("orderRecords", orders);
		mm.put("customer", customer);
		mm.put("deliverySurcharge", Cart._deliverySurcharge);
		// // get order details
		// Map<String, Object> orderMap = orderService.getOrderDetails(id);
		//
		// // place order details in request scope
		// mm.put("customer", orderMap.get("customer"));
		// mm.put("products", orderMap.get("products"));
		// mm.put("orderRecord", orderMap.get("orderRecord"));
		// mm.put("orderedProducts", orderMap.get("orderedProducts"));
		// mm.put("deliverySurcharge", Cart._deliverySurcharge);

		return "admin/customerOrders";
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
	public String editCustomerPost(final CustomerDto customerDto,
			final BindingResult bindingResult, ModelMap mm,
			HttpServletRequest request) {

		System.out.println("in customerEdit post");
		Integer id = 0;
		if (customerDto != null) {
			id = customerDto.getId();
		}

		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult error");
			mm.put("error", true);
			return "admin/customer";
		}

		// validate customer data
		boolean validationErrorFlag = false;
		validationErrorFlag = validator.validateCustomer(customerDto, request);

		if (validationErrorFlag == true) {
			mm.put("validationErrorFlag", validationErrorFlag);
			return "admin/customer";

			// otherwise, update customer to database
		} else {
			Customer updatedCustomer = customerDtoService.addNewCustomer(customerDto);
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
	public String productConsole(
			@RequestParam(value = "id", required = false) Integer id,
			ModelMap mm) {
		if (id != null) {
			mm.put("productList", productRepo.findByCategoryId(id));
		} else {
			mm.put("productList", productRepo.findAll());
		}
		ProductDto newProductDto = new ProductDto();
		mm.put("productDto", newProductDto);
		return "admin/products";
	}

	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute ProductDto productDto,
			ModelMap mm, @RequestPart("file") MultipartFile file) {
		Product product = productDtoService.addNewProduct(productDto);
	    MultipartFile productPicture = file;
//        String rootPath = System.getProperty("AffableBean");
//        rootPath = rootPath.replace("/null","");
        File dir = new File("src/main/resources/static/img/products");
	    String filename = dir.getAbsolutePath() + File.separator + productDto.getName() + ".png";
        System.out.println("filename: " + filename);
        try {
			productPicture.transferTo(new File(filename));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/admin/products";
	}

	@RequestMapping(value = "/product/update", method = RequestMethod.POST)
	public String updateProduct(@ModelAttribute ProductDto productDto,
			ModelMap mm, @RequestParam("file") MultipartFile file) {
		Product product = productDtoService.editProduct(productDto);
		String filename = "";
        File dir = new File("src/main/resources/static/img/products");
        if (!dir.exists())
			System.out.println("file directory not found: "+ dir.getPath());
        filename = dir.getAbsolutePath() + File.separator + productDto.getName() + ".png" ;
        // Create the file on server
        File serverFile = new File(filename);

        try {
			file.transferTo(new File(filename));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/admin/products";
	}

	@RequestMapping(value = "/product/edit/{id}", method = RequestMethod.GET)
	public String productEdit(@PathVariable("id") Integer id, ModelMap mm) {
		Product selectedProduct = productRepo.findById(id);
		ProductDto selectedProductDto = new ProductDto(selectedProduct);
		mm.put("productDto", selectedProductDto);
		return "admin/editproduct";
	}

	@RequestMapping(value = "/product/delete/{id}", method = RequestMethod.GET)
	public String productDelete(@PathVariable("id") Integer id, ModelMap mm) {
		productRepo.delete(id);
		return "redirect:/admin/products";
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
