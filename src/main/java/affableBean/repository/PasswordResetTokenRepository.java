package affableBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import affableBean.domain.Member;
import affableBean.domain.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByMember(Member member);

}