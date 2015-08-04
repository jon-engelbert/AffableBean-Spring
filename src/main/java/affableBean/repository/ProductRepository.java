package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findAll();
	
	Product findById(Long id);
	List<Product> findByCategoryId(Long id);
	
	Product findOneByName(String name);
	
	// more methods to follow
	

}
