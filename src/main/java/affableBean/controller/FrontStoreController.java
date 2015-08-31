package affableBean.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import affableBean.cart.Cart;
import affableBean.domain.Member;
import affableBean.domain.PaymentInfo;
import affableBean.domain.Product;
import affableBean.domain.Role;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.PaymentInfoRepository;
import affableBean.repository.OrderedProductRepository;
import affableBean.repository.ProductRepository;
import affableBean.repository.RoleRepository;
import affableBean.service.IMemberService;
import affableBean.service.MemberDto;
import affableBean.service.MemberService;
import affableBean.service.OrderService;
import affableBean.service.ProductDto;
import affableBean.service.ValidatorService;
import affableBean.validation.EmailExistsException;

@Controller
public class FrontStoreController {

//	@Autowired
//	private Cart cart;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private IMemberService customerService;

	@Autowired 
	private PaymentInfoRepository paymentInfoRepo;
	
	@Autowired 
	private MemberRepository memberRepo;
	
	@Autowired 
	private RoleRepository roleRepo;
	
	@Autowired 
	private CategoryRepository categoryRepo;
	
	@Autowired 
	private ProductRepository productRepo;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired 
	private CustomerOrderRepository customerOrderRepo;
	
	
	@Autowired 
	private OrderedProductRepository orderedProductRepo;
	
	@Autowired
    private OrderService orderService;
	
	@Autowired
	private ValidatorService validator;
	
	public boolean isSignedIn(HttpServletRequest request) {	// , Session session
		Member member = new Member();
		if (request.getRemoteUser() != null)  { // || (session.getAttribute("remoteuser") != null) {
			member = memberRepo.findOneByEmail(request.getRemoteUser());
//			session.setAttribute("customerLoggedIn", member);
//	    	session.setAttribute("custId", member.getId());
//			session.setAttribute("isSignedIn", true);
		}
		System.out.println("isSignedIn, customer: " + member);
		return request.getRemoteUser() != null;
	}

	public Member getCustomer(HttpServletRequest request) {
		Member member = null;
		if (request.getRemoteUser() != null) {	//  || (session.getAttribute("isSignedIn") != null && (Boolean) session.getAttribute("isSignedIn") == true)) {
			member = memberRepo.findOneByEmail(request.getRemoteUser());
//			session.setAttribute("customerLoggedIn", member);
//	    	session.setAttribute("custId", member.getId());
//			session.setAttribute("isSignedIn", true);
		}
		System.out.println("getCustomer, customer: " + member);
		return member;
	}

	/**
	 * Category
	 */

	@RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
	public String home(ModelMap mm, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		mm.put("categoryList", categoryRepo.findAll());
		if (null == session.getAttribute("language")) {
			session.setAttribute("language", "en");
		}
		return "front_store/home";
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String category(@RequestParam(value="id",defaultValue="1") Integer id, ModelMap mm, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		mm.put("productList", productRepo.findByCategoryId(id));
		mm.put("categoryList", categoryRepo.findAll());
		mm.put("id", id);
		session.setAttribute("selectedCategory", categoryRepo.findById(id));
		return "front_store/category";
	}

	@RequestMapping(value = "/chooseLanguage", method = RequestMethod.GET)
	public String setLanguage(@RequestParam(value="lang") String language, HttpServletRequest request, HttpSession session) {
		
		//TODO: this does not work correctly when you click on it from a POST page, such as after updating the cart
		// need to find a way around this
		
//		localeResolver.setLocale(request, response, new Locale(language));
		session.setAttribute("language", language);
		//code below is to parse referring URL to return user to same page
		String referrerStr = request.getHeader("referer");
		String pathStr = "home";
		String queryStr = "";
		try {
			
			URL url = new URL(referrerStr);
			pathStr = url.getPath();
		    queryStr = url.getQuery();
		}
		catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e.getMessage());
		}
		String returnStr = new String();
		returnStr += pathStr;
		if (queryStr != null)
			returnStr += "?" + queryStr;
		return "redirect:" + returnStr;
	}
	
	/**
	 * CRUD on shopping cart
	 */
	
	@RequestMapping(value="/addToCart", method = RequestMethod.POST)
	public String addToCart(@RequestParam("productId") Product product, HttpServletRequest request, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		
		if (product != null) {
			cart.addItem(product);
		}
		
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping(value = "/viewCart", method = RequestMethod.GET)
	public String cart(@RequestParam(value="clear", required=false, defaultValue = "false") boolean clear, HttpServletRequest request, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (clear) {
			cart.clear();
		}

		return "front_store/cart";
	}
	
	@RequestMapping(value= "/updateCart", method = RequestMethod.POST) 
	public String updateCart(@RequestParam(value="productId", required=true) String productId, 
			@RequestParam(value="quantity") String quantity, 
			HttpSession session,
			ModelMap mm) {

		Cart cart = (Cart) session.getAttribute("cart");
        boolean invalidEntry = validator.validateQuantity(productId, quantity);

        if (!invalidEntry) {

            Product product = productRepo.findById(Integer.valueOf(productId));
            cart.update(product, quantity);
        }

        return "front_store/cart";
	}
	
	@RequestMapping(value="/checkout", method=RequestMethod.GET)
	public String checkout(HttpSession session, HttpServletRequest request, 
			ModelMap mm) {
		Cart cart = (Cart) session.getAttribute("cart");
		Member member = getCustomer(request);
		Integer custId = member.getId();
//		Member member = new Member();
//		Integer custId = (Integer) session.getAttribute("custId");	// custId is set when a new customer registers or existing one logs in
		PaymentInfo newPaymentInfo = new PaymentInfo();
		if (cart != null) 
			cart.calculateTotal(Cart._deliverySurcharge.toString());

		if (custId != null) 
			member = memberRepo.findById(custId);
		if (member != null)
			newPaymentInfo = new PaymentInfo(member.getName(), member.getAddress(), member.getCityRegion(), "");
		
//		CustomerDto customerDto = new CustomerDto(newPaymentInfo, newCust);
		mm.put("paymentInfo", newPaymentInfo);

		return "front_store/checkout";
	}
	
	@RequestMapping(value= "/purchase", method = RequestMethod.POST) 
	public String purchase(@ModelAttribute final PaymentInfo paymentInfo, final BindingResult bindingResult, HttpSession session, HttpServletRequest request, ModelMap mm) {
		Cart cart = (Cart) session.getAttribute("cart");
		Double surcharge = Cart._deliverySurcharge;

		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult error");
			request.setAttribute("validationErrorFlag", true);
            request.setAttribute("deliverySurcharge", surcharge);
    		mm.put("paymentInfo", paymentInfo);
            return "front_store/checkout"; 
		}
        System.out.println("cart: " + cart);
		
		if (cart != null) {

            // extract user data from request
            String name = paymentInfo.getName();
//            String email = customer.getEmail();
//            String phone = paymentInfo.getPhone();
            String address = paymentInfo.getAddress();
            String cityRegion = paymentInfo.getCityRegion();
            String ccNumber = paymentInfo.getCcNumber();
            System.out.println("paymentInfo: " + paymentInfo);
            

            // validate user data
            boolean validationErrorFlag = false;
            validationErrorFlag = validator.validateForm(name, address, cityRegion, ccNumber, request);

            // if validation error found, return user to checkout
            if (validationErrorFlag == true) {
                request.setAttribute("validationErrorFlag", validationErrorFlag);
                return "front_store/checkout";

                // otherwise, save order to database
            } else {
//            	Member member = memberRepo.findByEmail(customer.getEmail());
//            	PaymentInfo paymentInfo = paymentInfoRepo.findOneByCcNumber(customer.getCcNumber());

//        		Object ob = session.getAttribute("customerLoggedIn");
        		Member member = getCustomer(request);
//        		if (ob instanceof Member)
//        			member = (Member)ob;
        		paymentInfo.setMember(member);
            	PaymentInfo newPaymentInfo = paymentInfoRepo.saveAndFlush(paymentInfo);
                Integer orderId = orderService.placeOrder(newPaymentInfo, cart);
        		mm.put("paymentInfo", newPaymentInfo);
//                request.setAttribute("paymentInfo", paymentInfo);

                // if order processed successfully send user to confirmation page
                if (orderId != 0) {

                	// reset the cart only, not the entire session... the user may want to remain logged in.
//                	DoInvalidateSession(request, session);
        			cart = new Cart();
        			session.setAttribute("cart", cart);

                    // get order details
                    Map<String, Object> orderMap = orderService.getOrderDetails(orderId);

                    // place order details in request scope
                    request.setAttribute("customer", orderMap.get("customer"));
                    request.setAttribute("products", orderMap.get("products"));
                    request.setAttribute("orderRecord", orderMap.get("orderRecord"));
                    request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));
                    request.setAttribute("deliverySurcharge", surcharge);

                    return "front_store/confirmation";

                // otherwise, send back to checkout page and display error
                } else {
                    request.setAttribute("orderFailureFlag", true);
                    return "front_store/checkout";
                }
            }
        }
		
		return "front_store/cart";
		
	}
	
	@RequestMapping(value= "/newMember", method = RequestMethod.GET) 
	public String newMember(HttpSession session, HttpServletRequest request, ModelMap mm) {

        LOGGER.info("in newMember: " + getCustomer(request));
		Member newMember = getCustomer(request);
		MemberDto memberDto = new MemberDto();
		if (newMember != null)
			memberDto = new MemberDto(newMember);
        LOGGER.info("in newMember, memberDto: " + memberDto);

		mm.put("memberDto", memberDto);
		
		return "front_store/memberregistration";
		
	}
	
    private Member createMemberAccount(final MemberDto accountDto) {
        Member registered = null;
        LOGGER.info("in createMemberAccount");
        try {
        	accountDto.setUsername(accountDto.getEmail());
            registered = customerService.registerNewMemberAccount(accountDto);
        } catch (final EmailExistsException e) {
            return null;
        }
        return registered;
    }
    
	@RequestMapping(value="/newMemberSubmit", method = RequestMethod.POST)
	public String newMemberSubmit(@Valid final MemberDto memberDto, 
			final BindingResult bindingResult, 
			HttpServletRequest request, 
			HttpSession session,
			ModelMap mm, final Errors errors) {
		
        LOGGER.info("Registering user account with information: {}", memberDto);

        final Member registered = createMemberAccount(memberDto);
        if (registered == null) {
            // result.rejectValue("email", "message.regError");
        	mm.put("memberDto", memberDto);
            return "front_store/memberregistration";
        }
        try {
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        } catch (final Exception ex) {
            LOGGER.warn("Unable to register user", ex);
//            return new ModelAndView("emailError", "memberDto", memberDto);
        	mm.put("memberDto", memberDto);
            return "front_store/memberregistration";
        }
//        return new ModelAndView("successRegister", "memberDto", memberDto);
        return "front_store/memberlogin";

        
//        if (bindingResult.hasErrors()) {
//			System.out.println("bindingResult error");
//			mm.put("validationErrorFlag", true);
//		}
//
//        // validate user data
//        boolean validationErrorFlag = false;
//        Member member = registerNewMemberAccount(memberDto);
////        validationErrorFlag = validator.validateMember(member, request);
//        
//        // check for existing email
//        boolean emailExists = false;
//        if (!validationErrorFlag) {
//        	emailExists = customerService.checkEmailExists(member.getEmail());
//        	if (emailExists) {
//        		mm.put("customerLoggedIn", member);
//        		mm.put("emailExists", emailExists);
//        		return "front_store/memberregistration";
//        	}
//        }
//
//        // if validation error found, return user to checkout
//        if (validationErrorFlag == true) {
//        	mm.put("validationErrorFlag", validationErrorFlag);
//        } else {
//            Role userRole = roleRepo.findByName("USER");
//			member.setRole(userRole);
//			member.setEnabled(true);
//        	Member newcust = customerService.saveNewCustomer(member);
//			System.out.println("new customer: " + newcust.toString());
//        	newcust.setPassword(""); //do not send password back to the browser!
//        	mm.put("customerLoggedIn", newcust);
//        	mm.put("success", true);
//        	
////        	session.setAttribute("remoteuser", newcust.getEmail());
//        	session.setAttribute("isSignedIn", true);
//    		session.setAttribute("custId", newcust.getId());
//    		String redirectPath = (String) session.getAttribute("redirect");
//    		if (redirectPath != null) {					// if there is a redirect session attr, then redirect
//    			Cart cart = (Cart)session.getAttribute("cart");
//    			session.removeAttribute("redirect");
//    			return "redirect:" + redirectPath;
//    		}
//        }
//	
//		return "front_store/memberregistration";
	}
	
//	@RequestMapping(value="/login", method = RequestMethod.GET)
//	public String custLogin() {
//		
//		return "front_store/memberlogin";
//	}
//
	
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST } )
	public String loginConsole(@RequestParam(value = "error", required = false) String error, ModelMap mm) {
//System.out.println("in loginConsole");
		if(error != null) {
			System.out.println("in loginConsole, failed: " + error);
			mm.put("message", "Login Failed!");
			return "front_store/memberlogin";
		} else {
			System.out.println("in loginConsole, success: ");
			mm.put("message", false);
			return "front_store/home/";
		}
	}

//	@RequestMapping(value="/login", method = RequestMethod.POST)
//	public String custLogin(@RequestParam("username") String email,
//			@RequestParam("password") String password, 
//			HttpSession session,
//			ModelMap mm) {
//		Member member = new Member();
//		member = memberRepo.findOneByEmail(email);
//		if (member == null || member.getId() == null) {
//			mm.put("loginerror", true);
//			return "front_store/memberlogin";
//		}
//		
//		boolean isPasswordValid = customerService.validatePassword(password, member.getPassword());
//		
//		if (!isPasswordValid) {
//			mm.put("loginerror", true);
//			return "front_store/memberlogin";
//		}
//
//    	System.out.println("customer " + member.getName() + " verified.  id: " + member.getId());
//    	System.out.println("customer role: " + member.getRole() + " name: " + member.getRole().getName());
//
//    	session.setAttribute("custId", member.getId());
//		session.setAttribute("isSignedIn", true);
//		String redirectPath = (String) session.getAttribute("redirect");
//		member.setPassword("");
//		session.setAttribute("customerLoggedIn", member);
//		
//		if (redirectPath != null) {					// if there is a redirect session attr, then redirect
//			mm.put("customerLoggedIn", member);
//			session.removeAttribute("redirect");
//			return "redirect:" + redirectPath;
//		}
//		return "redirect:/home";
//		
//	}
	
	@RequestMapping("/logout")
	public String customerLogout (HttpServletRequest request, HttpSession session) {
		
		DoInvalidateSession(request, session);
		
		return "redirect:/home";
	}
	


	private void DoInvalidateSession(HttpServletRequest request,
			HttpSession session) {
        // in case language was set using toggle, get language choice before destroying session
        Locale locale = (Locale) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale.session");
        String language = "";

        if (locale != null) {

            language = (String) locale.getLanguage();
        }

        // dissociate shopping cart from session
        session.setAttribute("cart", null);

        // end session
        session.invalidate();

        if (!language.isEmpty()) {                       // if user changed language using the toggle,
                                                         // reset the language attribute - otherwise
            request.setAttribute("language", language);  // language will be switched on confirmation page!
        }

	}



}
