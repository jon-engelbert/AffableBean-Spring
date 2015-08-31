package affableBean.service;

import org.springframework.data.domain.Page;

import affableBean.domain.Member;
import affableBean.validation.EmailExistsException;


public interface IMemberService {

    public Page<Member> findAllCustomers(Integer pageNumber);
    
    Member registerNewMemberAccount(MemberDto accountDto) throws EmailExistsException;

//    Member getMember(String verificationToken);

    Member saveNewCustomer(Member Member);

    void deleteMember(Member Member);

//    void createVerificationTokenForMember(Member Member, String token);
//
//    VerificationToken getVerificationToken(String VerificationToken);
//
//    VerificationToken generateNewVerificationToken(String token);

//    void createPasswordResetTokenForMember(Member Member, String token);

    Member getMemberByEmail(String email);

	Member getMemberById(Integer id);

//    Member getMemberByName(String name);

//    PasswordResetToken getPasswordResetToken(String token);

//    Member getMemberByPasswordResetToken(String token);

    void changeMemberPassword(Member Member, String password);

    boolean checkIfValidOldPassword(Member Member, String password);


}
