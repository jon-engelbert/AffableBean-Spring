package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Customer;
import affableBean.domain.CustomerOrder;

@Repository
public interface CustomerOrderRepository extends
		JpaRepository<CustomerOrder, Integer> {

	List<CustomerOrder> findAll();

	CustomerOrder findById(Integer id);
	
	List<CustomerOrder> findByCustomer(Customer customer);

	// more methods
}
