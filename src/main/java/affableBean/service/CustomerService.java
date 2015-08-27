package affableBean.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import affableBean.domain.Customer;
import affableBean.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

	@Resource
	private CustomerRepository customerRepo;
	
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	
	private static final int PAGE_SIZE = 5;

    public Page<Customer> findAllCustomers(Integer pageNumber) {
        PageRequest request =
            new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
        return customerRepo.findAll(request);
    }

	public List<Customer> getAll() {
		return customerRepo.findAll();
	}
	
	public Customer saveAndFlush(Customer customer) {
		return customerRepo.saveAndFlush(customer);
	}
	
	public Customer findOneByName(String name) {
		return customerRepo.findOneByName(name);
	
	}
	
	public Customer findById(Integer id) {
		return customerRepo.findById(id);
	}
	
	public Customer saveNewCustomer(Customer customer) {
		
		String password = customer.getPassword();
		
		
		String encodedPw = encoder.encode(password);
		
		customer.setPassword(encodedPw);
		
		
		return customerRepo.saveAndFlush(customer);
	}

	public boolean validatePassword(String rawPassword, String encodeddPassword) {
System.out.println("raw password, encoded: " + encoder.encode(rawPassword) + ", stored password: " + encodeddPassword);
		return (encoder.matches(rawPassword, encodeddPassword));
	}

	public boolean checkEmailExists(String email) {
		List<Customer> custs = new ArrayList<Customer>();
		custs = customerRepo.findByEmail(email);
		if (custs.size() > 0)
			return true;
		else return false;
	}
	
}
