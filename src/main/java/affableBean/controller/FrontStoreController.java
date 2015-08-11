package affableBean.controller;

import java.net.MalformedURLException;
import java.net.URL;

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
import affableBean.domain.Product;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.ProductRepository;
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
		request.setAttribute("validationErrorFlag", false);
		request.setAttribute("orderFailureFlag", false);
		
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping(value = "/viewCart", method = RequestMethod.GET)
	public String cart(@RequestParam(value="clear", required=false, defaultValue = "false") boolean clear, HttpServletRequest request, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (clear) {
			cart.clear();
		}
		request.setAttribute("validationErrorFlag", false);
		request.setAttribute("orderFailureFlag", false);
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
}
