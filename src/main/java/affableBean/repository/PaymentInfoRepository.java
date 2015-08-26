package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.PaymentInfo;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Integer> {

	List<PaymentInfo> findAll();

	PaymentInfo findById(Integer id);

	PaymentInfo findOneByName(String name);
	

	// more methods to follow

}
