package affableBean;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import affableBean.domain.Category;
import affableBean.domain.PaymentInfo;
import affableBean.domain.Product;
import affableBean.service.CategoryService;
import affableBean.service.MemberService;
import affableBean.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AffableBeanApplication.class)
@WebAppConfiguration
public class AffableBeanApplicationTests {

	@Autowired
	MemberService customerService;

	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;

	@Test
	public void contextLoads() {
	}

	@Test
	@Transactional
	public void testSaveCustomer() {
		PaymentInfo cust = new PaymentInfo(null, "Thien", "thienman@gmail.com",
				"7349724084", "test address", "MI",
				"1234 5678 9012 3456");

		PaymentInfo custReturned = customerService.saveAndFlush(cust);
		System.out.println("******" + custReturned.getPhone());
		PaymentInfo custSaved = customerService.findOneByName("Thien");
		Assert.assertEquals(custReturned, custSaved);

		Integer id = custReturned.getId();
		Assert.assertNotNull(id);

		return;

	}

	@Test
	@Transactional
	public void testNewCategoryAndProduct() {
		Category cat = new Category(null, "Canned Goods");

		Category catReturned = categoryService.saveAndFlush(cat);
		System.out.println("******" + catReturned.getName());
		Assert.assertNotNull(catReturned.getId());
		Category catSaved = categoryService.findOneByName("Canned Goods");
		Assert.assertEquals(catReturned, catSaved);

		Product prod = new Product(null, "Spam", new BigDecimal(3.49),
				"Pork shoulder and ham", new Date(), catSaved);
		Product prodReturned = productService.saveAndFlush(prod);
		System.out.println("******" + prodReturned.getName());
		Assert.assertNotNull(prodReturned.getId());
		Product prodSaved = productService.findOneByName("Spam");
		Assert.assertEquals(prodReturned, prodSaved);
		Assert.assertEquals(catSaved, prodSaved.getCategory());
		System.out.println("saved category id: " + catSaved.getId()
				+ ", saved product categry id: "
				+ prodSaved.getCategory().getId());
		return;

	}


}
