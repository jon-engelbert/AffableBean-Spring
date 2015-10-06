package affableBean.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;

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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import affableBean.AffableBeanApplication;
import affableBean.domain.Member;
import affableBean.domain.Role;
import affableBean.repository.MemberRepository;
import affableBean.repository.PaymentInfoRepository;
import affableBean.repository.RoleRepository;
import affableBean.service.MemberDto;

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
	}

	@After
	public void tearDown() throws Exception {
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
		mockMvc.perform(get("/newMember")).andExpect(
				view().name("registration/memberregistration"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	public void testNewMemberRegistrationPagePostSuccess() throws Exception {
		Role userRole = roleRepo.findByName("ROLE_USER");
		Member user = new Member("j", "j@j.com", "j@j.com", "Abcde01!@", false, userRole);	// Arrays.asList(userRole));
		MemberDto userDto = new MemberDto(user);
		userDto.setMatchingPassword(user.getPassword());
		System.out.println("userDto: " + userDto);
		mockMvc.perform(post("/newMemberSubmit")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.sessionAttr("memberDto", userDto)	// didn't work
				.param("name", userDto.getName())
				.param("username", userDto.getUsername())
				.param("email", userDto.getEmail())
				.param("password", userDto.getPassword())
				.param("matchingPassword", userDto.getPassword())
				)
				.andExpect(status().isOk())
				.andExpect(view().name("registration/successRegister"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	public void testNewMemberRegistrationPagePostPasswordMismatch() throws Exception {
		Role userRole = roleRepo.findByName("ROLE_USER");
		Member user = new Member("k", "k@k.com", "k@k.com", "Abcde01!@", false, userRole);	// Arrays.asList(userRole));
		MemberDto userDto = new MemberDto(user);
		userDto.setMatchingPassword(user.getPassword()+"!");
		System.out.println("userDto: " + userDto);
		mockMvc.perform(post("/newMemberSubmit")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.sessionAttr("memberDto", userDto)	// didn't work
				.param("name", userDto.getName())
				.param("username", userDto.getUsername())
				.param("email", userDto.getEmail())
				.param("password", userDto.getPassword())
				.param("matchingPassword", userDto.getMatchingPassword())
				)
				.andExpect(status().isOk())
				.andExpect(view().name("registration/memberregistration"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	public void testNewMemberRegistrationPagePostEmailInUse() throws Exception {
		Role userRole = roleRepo.findByName("ROLE_USER");
		Member user1 = new Member("jon", "jon@jon.com", "jon@jon.com", "Abcde01!@", false, userRole);	// Arrays.asList(userRole));
		memberRepo.save(user1);
		Member user = new Member("jon", "jon@jon.com", "jon@jon.com", "Abcde01!@", false, userRole);	// Arrays.asList(userRole));
		MemberDto userDto = new MemberDto(user);
		userDto.setMatchingPassword(user.getPassword());
		System.out.println("userDto: " + userDto);
		mockMvc.perform(post("/newMemberSubmit")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.sessionAttr("memberDto", userDto)	// didn't work
				.param("name", userDto.getName())
				.param("username", userDto.getUsername())
				.param("email", userDto.getEmail())
				.param("password", userDto.getPassword())
				.param("matchingPassword", userDto.getPassword())
				)
				.andExpect(status().isOk())
				.andExpect(view().name("registration/memberregistration"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

}
