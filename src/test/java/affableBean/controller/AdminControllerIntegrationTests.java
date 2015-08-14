package affableBean.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import affableBean.AffableBeanApplication;
import affableBean.domain.Member;
import affableBean.domain.Role;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.ProductRepository;
import affableBean.repository.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AffableBeanApplication.class)
@WebAppConfiguration
public class AdminControllerIntegrationTests {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private CategoryRepository categoryRepo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private MemberRepository memberRepo;
	@Autowired
	private RoleRepository roleRepo;
	@InjectMocks
	AdminController controller;
	MockMvc mockMvc;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoginConsole() throws Exception {
		mockMvc.perform(get("/admin/login")).andExpect(
				view().name("admin/login"));
	}

	@Test
	public void testCustomerConsole() throws Exception {
		// ArrayList<Customer> expectedCustomers = new ArrayList<Customer>();
		// Customer newCust = new Customer(0, "ted", "t@t.com", "123", "street",
		// "aa", "4567");
		// customerRepo.save(newCust);
		mockMvc.perform(get("/admin")).andExpect(view().name("admin/index"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(model().attributeExists("customerList"))
				.andExpect(model().attribute("customerList", hasSize(15)));
	}

	@Test
	public void testMemberConsole() throws Exception {
		Role adminRole = roleRepo.findByName("ADMIN");
//		roleRepo.save(newRole);
		Member newMember = new Member("jon", "jonny", "123", true, adminRole);
		memberRepo.save(newMember);
		mockMvc.perform(get("/admin/member")).andExpect(view().name("admin/member"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(model().attributeExists("memberList"))
				.andExpect(model().attribute("memberList", is(not(empty()))));
	}

	@Test
	public void testOrderConsole() {
		fail("Not yet implemented");
	}

	@Test
	public void testProductConsole() throws Exception {
		mockMvc.perform(get("/admin/product")).andExpect(view().name("admin/product"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("text/html;charset=UTF-8"))
		.andExpect(model().attributeExists("productList"))
		.andExpect(model().attribute("productList", hasSize(16)));
	}

	@Test
	public void testCategoryConsole() throws Exception {
		mockMvc.perform(get("/admin/category")).andExpect(view().name("admin/category"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("text/html;charset=UTF-8"))
		.andExpect(model().attributeExists("categoryList"))
		.andExpect(model().attribute("categoryList", hasSize(4)));
	}

	@Test
	public void testProductByCategory() throws Exception {
		mockMvc.perform(get("/admin/product?id=1")).andExpect(view().name("admin/product"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("text/html;charset=UTF-8"))
		.andExpect(model().attributeExists("productList"))
		.andExpect(model().attribute("productList", hasSize(4)));
	}

	@Test
	public void testAddCategory() {
		fail("Not yet implemented");
	}

	@Test
	public void testCategoryEdit() {
		fail("Not yet implemented");
	}

	@Test
	public void testCategoryDelete() {
		fail("Not yet implemented");
	}

}
