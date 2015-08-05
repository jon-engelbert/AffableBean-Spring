package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Category;

@Repository
public interface CustomerOrderRepository extends JpaRepository<Category, Integer> {
	
	List<Category> findAll();

	Category findById(Integer id);

	// more methods
}
