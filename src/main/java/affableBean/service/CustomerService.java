package affableBean.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

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

		return (encoder.matches(rawPassword, encodeddPassword));
	}
	
}
