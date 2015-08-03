package affableBean;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

//import spittr.domain.Spitter;
import affableBean.domain.Customer;


public class CustomerTest {

	@Test
	@Transactional
	public void count() {
//		assertEquals(4, customerRepository.count());
	}

	@Test
	@Transactional
	public void save_newCategory() {
//		assertEquals(4, customerRepository.count());
		Customer cust = new Customer(null, "newbee", "newbee@habuma.com", "123-456-7890", "newbee Walls", "aa", "123");
//		Customer saved = customerRepository.save(spitter);
//		assertEquals(5, customerRepository.count());
//		assertCustomer(4, saved);
//		assertCustomer(4, customerRepository.findOne(5L));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	
	private static Customer[] CUSTOMERS = new Customer[6];
	@BeforeClass
//	public Customer(Long id, String name, String email, String phone, String address, String city_region, String cc) {
	public static void setUpBeforeClass() throws Exception {
		CUSTOMERS[0] = new Customer(1L, "habuma", "hubama@h.com", "123-456-7890", "Craig Walls", "aa", "123");
		CUSTOMERS[1] = new Customer(2L, "mwalls", "mwalls@habuma.com", "123-456-7890", "Michael Walls", "aa", "123");
		CUSTOMERS[2] = new Customer(3L, "chuck", "chuck@habuma.com", "123-456-7890", "Chuck Wagon", "aa", "123");
		CUSTOMERS[3] = new Customer(4L, "artnames", "art@habuma.com", "123-456-7890", "Art Names", "aa", "123");
		CUSTOMERS[4] = new Customer(5L, "newbee", "newbee@habuma.com", "123-456-7890", "New Bee", "aa", "123");		
		CUSTOMERS[5] = new Customer(4L, "arthur", "arthur@habuma.com", "123-456-7890", "Arthur Names", "aa", "123");		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
