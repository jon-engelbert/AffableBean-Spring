package affableBean.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import affableBean.cart.Cart;
import affableBean.cart.CartItem;
import affableBean.domain.Customer;
import affableBean.domain.Product;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.OrderedProductRepository;
import affableBean.repository.ProductRepository;
import affableBean.service.OrderService;
import affableBean.service.ValidatorService;

@Controller
public class FrontStoreController {

//	@Autowired
//	private Cart cart;

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
	private OrderedProductRepository orderedProductRepo;
	
	@Autowired
    private OrderService orderService;
	
	private ValidatorService validator = new ValidatorService();

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
//		request.setAttribute("validationErrorFlag", false);
//		request.setAttribute("orderFailureFlag", false);
		
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping(value = "/viewCart", method = RequestMethod.GET)
	public String cart(@RequestParam(value="clear", required=false, defaultValue = "false") boolean clear, HttpServletRequest request, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (clear) {
			cart.clear();
		}
		// request.setAttribute("validationErrorFlag", false);
		// request.setAttribute("orderFailureFlag", false);
//		List<CartItem> items = cart.getItems();
//		double subTotal = cart.getSubtotal();
//		Integer numOfItems = cart.getNumberOfItems();
//		mm.put("itemList", items);
//		mm.put("numOfItems", numOfItems);
//		mm.put("subTotal", subTotal);
		return "front_store/cart";
	}
	
	@RequestMapping(value= "/updateCart", method = RequestMethod.POST) 
	public String updateCart(@RequestParam(value="productId", required=true) String productId, 
			@RequestParam(value="quantity") String quantity, 
			HttpSession session,
			ModelMap mm) {

		Cart cart = (Cart) session.getAttribute("cart");
        boolean validationErrorFlag = validator.validateQuantity(productId, quantity);

        if (!validationErrorFlag) {

            Product product = productRepo.findById(Integer.valueOf(productId));
            cart.update(product, quantity);
        }
        
        mm.put("validationErrorFlag",validationErrorFlag);
        mm.put("orderFailureFlag", false);

        return "front_store/cart";
	}
	
	@RequestMapping(value="/checkout", method=RequestMethod.GET)
	public String checkout(HttpSession session,
			ModelMap mm) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart != null) 
			cart.calculateTotal(cart._deliverySurcharge.toString());
//        mm.put("validationErrorFlag",false);
//        mm.put("orderFailureFlag", false);
		return "front_store/checkout";
	}
	
	@RequestMapping(value= "/purchase", method = RequestMethod.POST) 
	public String purchase(HttpSession session, HttpServletRequest request) {
		Cart cart = (Cart) session.getAttribute("cart");

		if (cart != null) {

            // extract user data from request
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String cityRegion = request.getParameter("cityRegion");
            String ccNumber = request.getParameter("creditcard");
            

            // validate user data
            boolean validationErrorFlag = false;
            validationErrorFlag = validator.validateForm(name, email, phone, address, cityRegion, ccNumber, request);

            // if validation error found, return user to checkout
            if (validationErrorFlag == true) {
                request.setAttribute("validationErrorFlag", validationErrorFlag);
                return "front_store/checkout";

                // otherwise, save order to database
            } else {

                System.out.println(" name: " + name + " email: " + email + " phone: " + phone + " address: " + address + " cityRegion: " + cityRegion + " ccNumber: " + ccNumber);
//                Customer newCust = customerRepo.saveAndFlush(new Customer(null, name, email, phone, address, cityRegion, ccNumber));
//                System.out.println("after saveandflush " + newCust.getName() + " " + newCust.getId());
                Integer orderId = orderService.placeOrder(name, email, phone, address, cityRegion, ccNumber, cart);

                // if order processed successfully send user to confirmation page
                if (orderId != 0) {

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

                    // get order details
                    Map<String, Object> orderMap = orderService.getOrderDetails(orderId);

                    // place order details in request scope
                    request.setAttribute("customer", orderMap.get("customer"));
                    request.setAttribute("products", orderMap.get("products"));
                    request.setAttribute("orderRecord", orderMap.get("orderRecord"));
                    request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));

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

}
