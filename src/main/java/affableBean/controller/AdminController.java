package affableBean.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.CustomerRepository;
import affableBean.repository.MemberRepository;
import affableBean.service.CustomerService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired 
	private CustomerRepository customerRepo;
	
//	@Autowired
//	private CustomerService customerService;
	
	@Autowired 
	private CustomerOrderRepository orderRepo;
	
//	@Autowired
//	private MemberService memberService;
	
	@Autowired
	private MemberRepository memberRepo;
	
//	@Autowired
//	private OrderService orderService;
	
	/**
	 * Auth process
	 */
	@RequestMapping(value = "/login", method= RequestMethod.GET )
	public String loginConsole(@RequestParam(value = "error", required = false) boolean error, ModelMap mm) {
		if(error == true) {
			mm.put("message", "Login Failed!");
		} else {
			mm.put("message", false);
		}
		return "admin_console/login";
	}
	
	/**
	 * Login succeeded, inside AdminConsole
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String customerConsole(ModelMap mm) {
		mm.put("customerList", customerRepo.findAll());
		return "admin/customer";
	}
	
	@RequestMapping(value = "/member", method = RequestMethod.GET)
	public String memberConsole(ModelMap mm) {
		mm.put("memberList", memberRepo.findAll());
		return "admin/member";
	}
	
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String orderConsole(ModelMap mm) {
		mm.put("orderList", orderRepo.findAll());
		return "admin/order";
	}
	

}
