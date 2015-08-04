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
	
}
