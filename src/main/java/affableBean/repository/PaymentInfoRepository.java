package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Member;
import affableBean.domain.PaymentInfo;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Integer> {

	List<PaymentInfo> findAll();

	PaymentInfo findOneById(Integer id);

	PaymentInfo findOneByName(String name);
	PaymentInfo findOneByCcNumber(String ccNumber);
	PaymentInfo findByOwner(Member owner);
	

	// more methods to follow

}
