package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	List<Product> findAll();
	
	Product findById(Integer id);

	List<Product> findByCategoryId(Integer id);
	
	Product findOneByName(String name);
	
	// more methods to follow
	

}
