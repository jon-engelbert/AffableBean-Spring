package affableBean.service;

import affableBean.domain.Member;
import affableBean.domain.PasswordResetToken;
import affableBean.domain.VerificationToken;
import affableBean.validation.EmailExistsException;

public interface IMemberService {

    Member registerNewMemberAccount(MemberDto accountDto) throws EmailExistsException;

    Member getMember(String verificationToken);

    void saveRegisteredMember(Member user);

    void deleteMember(Member user);

    void createVerificationTokenForMember(Member user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForMember(Member user, String token);

    Member findMemberByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Member getMemberByPasswordResetToken(String token);

    Member getMemberByID(long id);

    void changeMemberPassword(Member user, String password);

    boolean checkIfValidOldPassword(Member user, String password);

}