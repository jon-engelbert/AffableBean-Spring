package affableBean.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import affableBean.domain.Customer;
import affableBean.domain.Product;
import affableBean.repository.CustomerRepository;
import affableBean.repository.ProductRepository;

@Service
@Transactional
public class CustomerService {

	@Resource
	private CustomerRepository customerRepo;

	public List<Customer> getAll() {
		return customerRepo.findAll();
	}
	
	public Customer saveAndFlush(Customer customer) {
		return customerRepo.saveAndFlush(customer);
	}
	
	public Customer findOneByName(String name) {
		return customerRepo.findOneByName(name);
	
	}
	
	public Customer findById(Long id) {
		return customerRepo.findById(id);
	}
	
}
