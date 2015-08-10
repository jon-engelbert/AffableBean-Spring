package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	List<Member> findAll();

//	List<Member> findByUsername(String username);
	
	Member findByUsername(String username);

}