package affableBean.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import affableBean.domain.Customer;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.ProductRepository;

//@RunWith(SpringJUnit4ClassRunner.class)
public class AdminControllerTests {

	@Mock
//	@Autowired
	private CustomerRepository customerRepo;
	@Mock
//	@Autowired
	private CategoryRepository categoryRepo;
	@Mock
//	@Autowired
	private ProductRepository productRepo;
	@Mock
//	@Autowired
	private MemberRepository memberRepo;
    @InjectMocks
    AdminController controller;
    MockMvc mockMvc;
    
//    @Mock
//    CustomerRepository mockCustomerRepo;
    
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
		mockMvc.perform(get("/admin/login")).andExpect(
				view().name("admin/login"));
	}

	@Test
	public void testCustomerPage() throws Exception {
		ArrayList<Customer> expectedCustomers = new ArrayList<Customer>();
        when(customerRepo.findAll()).thenReturn(expectedCustomers);
		mockMvc.perform(get("/admin")).andExpect(view().name("admin/customer"));
	}

	@Test
	public void testMemberPage() throws Exception {
		mockMvc.perform(get("/admin/member")).andExpect(view().name("admin/member"));
	}


}
