package affableBean.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;

import javax.servlet.Filter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import affableBean.AffableBeanApplication;
import affableBean.cart.Cart;
import affableBean.domain.Member;
import affableBean.domain.Product;
import affableBean.domain.Role;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.PaymentInfoRepository;
import affableBean.repository.ProductRepository;
import affableBean.repository.RoleRepository;
import affableBean.service.IMemberService;
import affableBean.service.MemberDto;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AffableBeanApplication.class)
//@ContextConfiguration(classes = AffableBeanApplication.class)
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)	
@WebAppConfiguration
public class FrontStoreControllerIntegrationTests {

	@Autowired
	private WebApplicationContext wac;
	@Autowired 
    MockHttpSession mockSession;
//	@Autowired
//    MockHttpServletRequest mockRequest;
	@Autowired
	MemberRepository customerRepo;
	@Autowired
	private CategoryRepository categoryRepo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private MemberRepository memberRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private CustomerOrderRepository orderRepo;
	@Autowired
	private PaymentInfoRepository paymentInfoRepo;
	@Autowired
    private IMemberService userService;
	@Autowired
	private Filter springSecurityFilterChain;
	
	@Mock
	private PaymentInfoRepository mockPaymentInfoRepo;
	
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
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
	              .addFilters(springSecurityFilterChain)
	              .apply(springSecurity())
	              .build();
	    mockSession = new MockHttpSession();
//	    mockRequest = new MockHttpServletRequest();
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
//	@WithMockUser("user@user.com")
	@WithMockUser(username="admin@admin.com",roles={"USER","ADMIN"})
	public void testCheckout() throws Exception {
		Cart cart = new Cart();
		Product product = productRepo.findOne(1);
		System.out.println("in testCheckout, product: " + product);
		cart.addItem(product);
		Role userRole = roleRepo.findByName("ROLE_USER");
		Member user = new Member("jon", "jon@jon.com", "jon@jon.com", "Abcde01!@", false, Arrays.asList(userRole));
		memberRepo.save(user);
		MemberDto userDto = new MemberDto(user);
		userDto.setMatchingPassword(user.getPassword());
		mockSession.setAttribute("cart", cart);
		System.out.println("about to get checkout");
		mockMvc.perform(get("/checkout")
				.with(user("admin@admin.com").password("pass").roles("USER","ADMIN"))	
				.session(mockSession)
//				.with(new RequestPostProcessor() { 
//				    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
//				       request.setRemoteUser("jon@jon.com"); 
//				       return request;
//				    }})				
				    				    )
		.andExpect(view().name("front_store/checkout"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

}
