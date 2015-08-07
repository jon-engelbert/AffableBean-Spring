package affableBean.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import affableBean.domain.Category;
import affableBean.domain.Customer;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.ProductRepository;

public class FrontStoreControllerTests {

	@Mock
	private CustomerRepository customerRepo;
	@Mock
	private CategoryRepository categoryRepo;
	@Mock
	private ProductRepository productRepo;
	@Mock
	private MemberRepository memberRepo;

	@InjectMocks
	FrontStoreController controller;
	MockMvc mockMvc;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = standaloneSetup(controller).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHomePage() throws Exception {
		mockMvc.perform(get("/affable")).andExpect(
				view().name("front_store/home"));
		mockMvc.perform(get("/")).andExpect(view().name("front_store/home"));
	}

	@Test
	public void testCategoryPage() throws Exception {
		ArrayList<Category> expectedCats = new ArrayList<Category>();
		expectedCats.add(new Category(0, "stuff"));
		when(categoryRepo.findAll()).thenReturn(expectedCats);
		mockMvc.perform(get("/category"))
			.andExpect(view().name("front_store/category"));
//			.andExpect(model().attribute("name", hasProperty("name", is("stuff"))));
	}

}
