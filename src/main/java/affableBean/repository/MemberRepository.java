package affableBean.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import affableBean.domain.Member;

public interface MemberRepository extends Repository<Member, Integer> {
	
	List<Member> findAll();
	
	List<Member> findByUsername(String username);
	
}