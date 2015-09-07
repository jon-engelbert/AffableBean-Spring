package affableBean.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import affableBean.domain.PaymentInfo;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.PaymentInfoRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.ProductRepository;
import affableBean.service.ValidatorService;

//@RunWith(SpringJUnit4ClassRunner.class)
public class AdminControllerTests {

	@Mock
	// @Autowired
	private PaymentInfoRepository customerRepo;
	@Mock
	// @Autowired
	private CategoryRepository categoryRepo;
	@Mock
	// @Autowired
	private ProductRepository productRepo;
	@Mock
	// @Autowired
	private MemberRepository memberRepo;
	@Mock
	// @Autowired
	private ValidatorService validator;
	@Mock 
	private CustomerOrderRepository orderRepo;
	@InjectMocks
	AdminController controller;
	MockMvc mockMvc;

	// @Mock
	// CustomerRepository mockCustomerRepo;

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
	public void testMemberPage() throws Exception {
		mockMvc.perform(get("/admin/member")).andExpect(
				view().name("admin/member"))
				.andExpect(status().isOk());
	}

	@Test
	public void testEditCustomerPage() throws Exception {
		mockMvc.perform(get("/admin/customerEdit?id=1")).andExpect(
				view().name("admin/customer"))
				.andExpect(status().isOk());
	}

	@Test
	public void testSubmitCustomerPage() throws Exception {
		mockMvc.perform(post("/admin/customerEdit")).andExpect(
				view().name("admin/customer"))
				.andExpect(status().isOk());
	}

	@Test
	public void testCustomerRecordPage() throws Exception {
		mockMvc.perform(get("/admin/customerRecord?id=1")).andExpect(
				view().name("admin/viewCustomer"))
				.andExpect(status().isOk());
	}

}
