package affableBean.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.*;

import affableBean.AffableBeanApplication;
import affableBean.domain.Member;
import affableBean.domain.Role;
import affableBean.repository.MemberRepository;
import affableBean.repository.PaymentInfoRepository;
import affableBean.repository.RoleRepository;
import affableBean.service.MemberDto;
import affableBean.service.PasswordDto;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AffableBeanApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)	
public class RegistrationControllerIntegrationTests {

	@Autowired
	private WebApplicationContext wac;
	@Autowired
	MemberRepository customerRepo;
	@Autowired
	private MemberRepository memberRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private PaymentInfoRepository paymentInfoRepo;
	@Autowired
	private FilterChainProxy springSecurityFilter;
	
	@Mock
	private PaymentInfoRepository mockPaymentInfoRepo;
	
	@InjectMocks
	RegistrationController controller;
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
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getName()).thenReturn("admin@admin.com");
		SecurityContextHolder.setContext(securityContext);		
	}

	@After
	public void tearDown() throws Exception {
	}

	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
			e.printStackTrace();
	        throw new RuntimeException(e);
	    }
	}  

	@Test
	public void testLoginPage() throws Exception {
		mockMvc.perform(get("/login")).andExpect(
				view().name("admin/login"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	public void testNewMemberRegistrationPageGet() throws Exception {
		HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
		CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
		mockMvc
			.perform(get("/newMember").with(csrf().asHeader()))
			.andExpect(
				view().name("registration/memberregistration"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.TEXT_HTML_VALUE +";charset=UTF-8"));
	}

	@Test
	public void testNewMemberRegistrationPagePostSuccess() throws Exception {
		Role userRole = roleRepo.findByName("ROLE_USER");
		Member user = new Member("j", "jon@jon.com", "joe@joe.com", "Abcde012", true, Arrays.asList(userRole));
		MemberDto userDto = new MemberDto();
		userDto.CopyFromModel(user);
		userDto.setMatchingPassword(user.getPassword());
		userDto.setId(-1);
		userDto.setAddress("addr");;
		userDto.setPhone("phone");
		System.out.println("userDto: " + userDto);
//		Hibernate.initialize(user.getRoles());
		mockMvc.perform(post("/user/registration")
				  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				  .content(userDto)
				.param("name", userDto.getName())
				.param("email", userDto.getEmail())
				.param("password", userDto.getPassword())
				.param("matchingPassword", userDto.getPassword())
				)
				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.street").value("12345 Horton Ave"))
//				.andExpect(view().name("registration/successRegister"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE +";charset=UTF-8"))
				.andExpect(jsonPath("$.status", is("success")));
	}

	@Test
	public void testNewMemberRegistrationPagePostPasswordMismatch() throws Exception {
		Role userRole = roleRepo.findByName("ROLE_USER");
		Member user = new Member("k", "k@k.com", "k@k.com", "Abcde01!@", false, Arrays.asList(userRole));
		MemberDto userDto = new MemberDto();
		userDto.CopyFromModel(user);
		userDto.setMatchingPassword(user.getPassword()+"!");
		System.out.println("userDto: " + userDto);
		mockMvc.perform(post("/user/registration")
				  .content(asJsonString(userDto))
				  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				  .accept(MediaType.APPLICATION_JSON)
//				.sessionAttr("memberDto", userDto)	// didn't work
				.param("name", userDto.getName())
				.param("email", userDto.getEmail())
				.param("password", userDto.getPassword())
				.param("matchingPassword", userDto.getMatchingPassword())
				)
				.andExpect(status().isOk())
//				.andExpect(view().name("registration/memberregistration"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE +";charset=UTF-8"))
				.andExpect(jsonPath("$.status", is("failure")));
	}

	@Test
	public void testNewMemberRegistrationPagePostEmailInUse() throws Exception {
		Role userRole = roleRepo.findByName("ROLE_USER");
		Member user1 = new Member("jon", "jon@jon.com", "jon@jon.com", "Abcde01!@", false, Arrays.asList(userRole));
		memberRepo.save(user1);
		Member user = new Member("jon", "jon@jon.com", "jon@jon.com", "Abcde01!@", false, Arrays.asList(userRole));
		MemberDto userDto = new MemberDto();
		userDto.CopyFromModel(user);
		userDto.setMatchingPassword(user.getPassword());
		System.out.println("userDto: " + userDto);
		mockMvc.perform(post("/user/registration")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				  .content(asJsonString(userDto))
//				  .accept(MediaType.APPLICATION_JSON)
//				.sessionAttr("member", userDto)	// didn't work
				.param("name", userDto.getName())
				.param("email", userDto.getEmail())
				.param("password", userDto.getPassword())
				.param("matchingPassword", userDto.getPassword())
				)
				// failure....
				.andExpect(status().isOk())
//				.andExpect(view().name("registration/memberregistration"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE +";charset=UTF-8"))
				.andExpect(jsonPath("$.status", is("failure")));
	}

	@Test
	public void testSavePasswordFromScratchPostSuccess() throws Exception {
//		when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(new Authentication());
		PasswordDto pwDto = new PasswordDto();
		pwDto.setOldPassword("old");
		pwDto.setPassword("Black123");
		pwDto.setMatchingPassword(pwDto.getPassword());
		System.out.println("pwDto: " + pwDto);
//		Hibernate.initialize(user.getRoles());
		mockMvc.perform(post("/user/savePasswordFromScratch")
				  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				  .content(userDto)
				.param("password", pwDto.getPassword())
				.param("matchingPassword", pwDto.getPassword())
				)
				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.street").value("12345 Horton Ave"))
//				.andExpect(view().name("registration/successRegister"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE +";charset=UTF-8"))
				.andExpect(jsonPath("$.status", is("success")));
		}

}
