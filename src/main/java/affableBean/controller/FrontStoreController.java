package affableBean.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import affableBean.cart.Cart;
import affableBean.domain.Customer;
import affableBean.domain.Product;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.OrderedProductRepository;
import affableBean.repository.ProductRepository;
import affableBean.service.CustomerDto;
import affableBean.service.CustomerDtoService;
import affableBean.service.CustomerService;
import affableBean.service.OrderService;
import affableBean.service.ProductDto;
import affableBean.service.ValidatorService;

@Controller
public class FrontStoreController {

//	@Autowired
//	private Cart cart;

	@Autowired
	private CustomerDtoService customerDtoService;
	
	@Autowired 
	private CustomerRepository customerRepo;
	
	@Autowired 
	private CategoryRepository categoryRepo;
	
	@Autowired 
	private ProductRepository productRepo;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired 
	private CustomerOrderRepository customerOrderRepo;
	
	@Autowired 
	CustomerService customerService;
	
	@Autowired 
	private OrderedProductRepository orderedProductRepo;
	
	@Autowired
    private OrderService orderService;
	
	@Autowired
	private ValidatorService validator;

	/**
	 * Category
	 */

	@RequestMapping(value = {"home", "/"}, method = RequestMethod.GET)
	public String home(ModelMap mm, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		mm.put("categoryList", categoryRepo.findAll());
		if (null == session.getAttribute("language")) {
			session.setAttribute("language", "en");
		}
		return "front_store/home";
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String category(@RequestParam(value="id",defaultValue="1") Integer id, ModelMap mm, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		mm.put("productList", productRepo.findByCategoryId(id));
		mm.put("categoryList", categoryRepo.findAll());
		mm.put("id", id);
		session.setAttribute("selectedCategory", categoryRepo.findById(id));
		return "front_store/category";
	}

	@RequestMapping(value = "/chooseLanguage", method = RequestMethod.GET)
	public String setLanguage(@RequestParam(value="lang") String language, HttpServletRequest request, HttpSession session) {
		
		//TODO: this does not work correctly when you click on it from a POST page, such as after updating the cart
		// need to find a way around this
		
//		localeResolver.setLocale(request, response, new Locale(language));
		session.setAttribute("language", language);
		//code below is to parse referring URL to return user to same page
		String referrerStr = request.getHeader("referer");
		String pathStr = "home";
		String queryStr = "";
		try {
			
			URL url = new URL(referrerStr);
			pathStr = url.getPath();
		    queryStr = url.getQuery();
		}
		catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e.getMessage());
		}
		String returnStr = new String();
		returnStr += pathStr;
		if (queryStr != null)
			returnStr += "?" + queryStr;
		return "redirect:" + returnStr;
	}
	
	/**
	 * CRUD on shopping cart
	 */
	
	@RequestMapping(value="/addToCart", method = RequestMethod.POST)
	public String addToCart(@RequestParam("productId") Product product, HttpServletRequest request, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		
		if (product != null) {
			cart.addItem(product);
		}
		
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping(value = "/viewCart", method = RequestMethod.GET)
	public String cart(@RequestParam(value="clear", required=false, defaultValue = "false") boolean clear, HttpServletRequest request, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (clear) {
			cart.clear();
		}

		return "front_store/cart";
	}
	
	@RequestMapping(value= "/updateCart", method = RequestMethod.POST) 
	public String updateCart(@RequestParam(value="productId", required=true) String productId, 
			@RequestParam(value="quantity") String quantity, 
			HttpSession session,
			ModelMap mm) {

		Cart cart = (Cart) session.getAttribute("cart");
        boolean invalidEntry = validator.validateQuantity(productId, quantity);

        if (!invalidEntry) {

            Product product = productRepo.findById(Integer.valueOf(productId));
            cart.update(product, quantity);
        }

        return "front_store/cart";
	}
	
	@RequestMapping(value="/checkout", method=RequestMethod.GET)
	public String checkout(HttpSession session,
			ModelMap mm) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart != null) 
			cart.calculateTotal(Cart._deliverySurcharge.toString());
		Object ob = session.getAttribute("customerLoggedIn");
		Customer newCust = new Customer();
		if (ob instanceof Customer)
			newCust = (Customer)ob;
		CustomerDto customerDto = new CustomerDto(newCust);
		mm.put("customerDto", customerDto);

		return "front_store/checkout";
	}
	
	@RequestMapping(value= "/purchase", method = RequestMethod.POST) 
	public String purchase(final Customer customer, final BindingResult bindingResult, HttpSession session, HttpServletRequest request) {
		Cart cart = (Cart) session.getAttribute("cart");
		Double surcharge;
		
		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult error");
			request.setAttribute("validationErrorFlag", true);
            return "front_store/checkout"; 
		}
		
		if (cart != null) {

            // extract user data from request
            String name = customer.getName();
            String email = customer.getEmail();
            String phone = customer.getPhone();
            String address = customer.getAddress();
            String cityRegion = customer.getCityRegion();
            String ccNumber = customer.getCcNumber();
            surcharge = Cart._deliverySurcharge;
            

            // validate user data
            boolean validationErrorFlag = false;
            validationErrorFlag = validator.validateForm(name, email, phone, address, cityRegion, ccNumber, request);

            // if validation error found, return user to checkout
            if (validationErrorFlag == true) {
                request.setAttribute("validationErrorFlag", validationErrorFlag);
                return "front_store/checkout";

                // otherwise, save order to database
            } else {
                Integer orderId = orderService.placeOrder(customer, cart);

                // if order processed successfully send user to confirmation page
                if (orderId != 0) {

                	// session and request invalidate and reset
                	DoInvalidateSession(request, session);
                    // get order details
                    Map<String, Object> orderMap = orderService.getOrderDetails(orderId);

                    // place order details in request scope
                    request.setAttribute("customer", orderMap.get("customer"));
                    request.setAttribute("products", orderMap.get("products"));
                    request.setAttribute("orderRecord", orderMap.get("orderRecord"));
                    request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));
                    request.setAttribute("deliverySurcharge", surcharge);

                    return "front_store/confirmation";

                // otherwise, send back to checkout page and display error
                } else {
                    request.setAttribute("orderFailureFlag", true);
                    return "front_store/checkout";
                }
            }
        }
		
		return "front_store/cart";
		
	}
	
	@RequestMapping(value= "/newcust", method = RequestMethod.GET) 
	public String newCust(HttpSession session, ModelMap mm) {

		Object ob = session.getAttribute("customerLoggedIn");
		Customer newCust = new Customer();
		if (ob instanceof Customer)
			newCust = (Customer)ob;
		CustomerDto customerDto = new CustomerDto(newCust);
		mm.put("customerDto", customerDto);
		
		return "front_store/customerregistration";
		
	}
	
	@RequestMapping(value="/newCustSubmit", method = RequestMethod.POST)
	public String newCustSubmit(@ModelAttribute final CustomerDto customerDto, 
			final BindingResult bindingResult, 
			HttpServletRequest request, 
			HttpSession session,
			ModelMap mm) {
		
		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult error");
			mm.put("validationErrorFlag", true);
		}

        // validate user data
        boolean validationErrorFlag = false;
        Customer customer = customerDtoService.addNewCustomer(customerDto);
        validationErrorFlag = validator.validateCustomer(customer, request);
        
        // check for existing email
        boolean emailExists = false;
        if (!validationErrorFlag) {
        	emailExists = customerService.checkEmailExists(customer.getEmail());
        	if (emailExists) {
        		mm.put("customer", customer);
        		mm.put("emailExists", emailExists);
        	}
        }

        // if validation error found, return user to checkout
        if (validationErrorFlag == true) {
        	mm.put("validationErrorFlag", validationErrorFlag);
        } else {
        	Customer newcust = customerService.saveNewCustomer(customer);
        	newcust.setPassword(""); //do not send password back to the browser!
        	mm.put("customer", newcust);
        	mm.put("success", true);
        	
    		session.setAttribute("isSignedIn", true);
    		newcust.setPassword("");
    		session.setAttribute("customerLoggedIn", newcust);

        }
	
		return "front_store/customerregistration";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String custLogin() {
		
		return "front_store/customerlogin";
	}

	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String custLogin(@RequestParam("email") String email,
			@RequestParam("password") String password, 
			HttpSession session,
			ModelMap mm) {
		Customer customer = new Customer();
		customer = customerRepo.findByEmail(email);
		if (customer == null || customer.getId() == null) {
			mm.put("loginerror", true);
			System.out.println("customer by email not found");
			return "front_store/customerlogin";
		}
		
		boolean isPasswordValid = customerService.validatePassword(password, customer.getPassword());
		
		if (!isPasswordValid) {
			mm.put("loginerror", true);
			System.out.println("password not valid");
			return "front_store/customerlogin";
		}

    	System.out.println("customer " + customer.getName() + " verified.  id: " + customer.getId());

		session.setAttribute("isSignedIn", true);
		customer.setPassword("");
		session.setAttribute("customerLoggedIn", customer);
		return "redirect:/home";
		
	}
	
	@RequestMapping("/logout")
	public String customerLogout (HttpServletRequest request, HttpSession session) {
		
		DoInvalidateSession(request, session);
		
		return "redirect:/home";
	}
	


	private void DoInvalidateSession(HttpServletRequest request,
			HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
        // in case language was set using toggle, get language choice before destroying session
        Locale locale = (Locale) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale.session");
        String language = "";

        if (locale != null) {

            language = (String) locale.getLanguage();
        }

        // dissociate shopping cart from session
        cart = null;

        // end session
        session.invalidate();

        if (!language.isEmpty()) {                       // if user changed language using the toggle,
                                                         // reset the language attribute - otherwise
            request.setAttribute("language", language);  // language will be switched on confirmation page!
        }

	}



}
