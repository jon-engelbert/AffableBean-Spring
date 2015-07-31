package affableBean.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Product;

@Repository
public interface ProductRepository extends
		PagingAndSortingRepository<Product, Long> {
	
	List<Product> findAll();
	
	Product findById(Long id);
	
	// more methods to follow
	

}
