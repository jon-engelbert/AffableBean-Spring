package affableBean.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import affableBean.CartItem;
import affableBean.domain.Category;
import affableBean.domain.Product;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.ProductRepository;
import affableBean.service.Cart;

@Controller
public class FrontStoreController {

	@Autowired
	private Cart cart;

	@Autowired 
	private CustomerRepository customerRepo;
	
	@Autowired 
	private CategoryRepository categoryRepo;
	@Autowired 
	private ProductRepository productRepo;
	
	@Autowired
	LocaleResolver localeResolver;

	/**
	 * Category
	 */

	@RequestMapping(value = {"home", "/"}, method = RequestMethod.GET)
	public String home(ModelMap mm, HttpSession session) {
		mm.put("categoryList", categoryRepo.findAll());
		if (null == session.getAttribute("language")) {
			session.setAttribute("language", "en");
		}
		return "front_store/home";
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String category(@RequestParam(value="id",defaultValue="1") Integer id, ModelMap mm, HttpSession session) {
		Category selectedCategory = new Category();
		mm.put("productList", productRepo.findByCategoryId(id));
		mm.put("categoryList", categoryRepo.findAll());
		mm.put("id", id);
		mm.put("selectedCategory", selectedCategory);
		return "front_store/category";
	}

	@RequestMapping("/chooseLanguage")
	public String setLanguage(@RequestParam(value="lang") String language, HttpServletRequest request, HttpSession session) {
//		localeResolver.setLocale(request, response, new Locale(language));
//		System.out.println(language);
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
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String cart(ModelMap mm) {
		List<CartItem> items = cart.getItems();
		double subTotal = cart.getSubtotal();
		Integer numOfItems = cart.getNumberOfItems();
		mm.put("itemList", items);
		mm.put("numOfItems", numOfItems);
		mm.put("subTotal", subTotal);
		return "front_store/cart";
	}
	
	@RequestMapping(value= "/updateCart", method = RequestMethod.POST) 
	public String updateCart(@ModelAttribute CartItem cartItem, Model model) {

            // get input from request

            boolean invalidEntry = false;	// validator.validateQuantity(cartItem.getProduct().getId(), cartItem.getQuantity());

            if (!invalidEntry) {

                Product product = cartItem.getProduct();
//                cart.update(product, cartItem.getQuantity());
            }

            return "/cart";
	}
}
