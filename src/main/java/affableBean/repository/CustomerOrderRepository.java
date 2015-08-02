package affableBean.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Category;

@Repository
public interface CustomerOrderRepository extends PagingAndSortingRepository<Category, Long> {
	
	List<Category> findAll();

	Category findById(Long id);

	// more methods
}
