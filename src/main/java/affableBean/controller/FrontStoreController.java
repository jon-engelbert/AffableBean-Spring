package affableBean.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

	/**
	 * Category
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(ModelMap mm) {
		mm.put("categoryList", categoryRepo.findAll());
		return "front_store/home";
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String category(@RequestParam(value="id",required=false) Integer id, ModelMap mm) {
		mm.put("productList", productRepo.findByCategoryId(id));
		mm.put("categoryList", categoryRepo.findAll());
		mm.put("id", id);
		return "front_store/category";
	}

	/**
	 * CRUD on shopping cart
	 */
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String cart(ModelMap mm) {
		Map<Product, Integer> itemMap = cart.getItems();
//		double subTotal = cart.calculateSubTotal();
//		Integer numOfItems = cart.sumQuantity();
//		mm.put("itemMap", itemMap);
//		mm.put("numOfItems", numOfItems);
//		mm.put("subTotal", subTotal);
		return "front_store/cart";
	}

}
