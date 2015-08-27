package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	List<Customer> findAll();

	Customer findById(Integer id);

	Customer findOneByName(String name);
	
	List<Customer> findByEmail(String email);
	
	Customer findOneByEmail(String email);

	// more methods to follow

}
