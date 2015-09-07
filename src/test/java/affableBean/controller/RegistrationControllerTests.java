package affableBean.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import affableBean.repository.MemberRepository;
import affableBean.service.ValidatorService;

//@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationControllerTests {

	@Mock
	// @Autowired
	private MemberRepository memberRepo;
	@Mock
	// @Autowired
	private ValidatorService validator;
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
	public void testLoginPage() throws Exception {
		mockMvc.perform(get("/login")).andExpect(
				view().name("admin/login"))
				.andExpect(status().isOk());
//				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}


}
