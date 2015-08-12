package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.OrderedProduct;

@Repository
public interface OrderedProductRepository extends
		JpaRepository<OrderedProduct, Integer> {
	
	List<OrderedProduct> findByCustomerOrderId(int id);

}