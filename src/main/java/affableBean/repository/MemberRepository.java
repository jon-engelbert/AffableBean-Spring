package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Member;
import affableBean.domain.PaymentInfo;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	List<Member> findAll();

//	List<Member> findByUsername(String username);
	
	Member findOneByUsername(String username);

	Member findOneByEmail(String email);

	Member findById(Integer id);

	Member findOneByName(String name);
}