package affableBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import affableBean.domain.Member;
import affableBean.domain.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByMember(Member member);

}