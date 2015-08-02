package affableBean.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Customer;

@Repository
public interface CustomerRepository extends
		PagingAndSortingRepository<Customer, Long> {
	
	List<Customer> findAll();
	
	Customer findById(Long id);
	
	// more methods to follow
	

}
