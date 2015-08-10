package affableBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import affableBean.domain.Member;
import affableBean.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	List<Role> findAll();

//	List<Member> findByUsername(String username);
	
	Role findByName(String name);


}