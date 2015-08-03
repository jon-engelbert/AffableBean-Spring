package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Category;

@Repository
public interface CustomerOrderRepository extends JpaRepository<Category, Long> {
	
	List<Category> findAll();

	Category findById(Long id);

	// more methods
}
