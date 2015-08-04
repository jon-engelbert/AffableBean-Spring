package affableBean.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import affableBean.domain.Product;
import affableBean.service.Cart;
import affableBean.service.CategoryService;
import affableBean.service.CustomerService;
import affableBean.service.ProductService;

@Controller
public class FrontStoreController {

	@Autowired
	private Cart cart;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerService customerService;

//	@Autowired
//	private OrderedProductService orderedProductService;
//
//	@Autowired
//	private OrderService orderService;

	/**
	 * Category
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(ModelMap mm) {
		mm.put("categoryList", categoryService.findAll());
		return "front_store/home";
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String category(@RequestParam("id") Long id, ModelMap mm) {
		mm.put("productList", productService.getByCategoryId(id));
		mm.put("categoryList", categoryService.findAll());
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
