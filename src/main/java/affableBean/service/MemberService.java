package affableBean.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import affableBean.domain.Member;
import affableBean.domain.PaymentInfo;
import affableBean.domain.Role;
import affableBean.repository.MemberRepository;
import affableBean.repository.PaymentInfoRepository;
import affableBean.repository.RoleRepository;
import affableBean.validation.EmailExistsException;

@Service
@Transactional
public class MemberService implements IMemberService{


    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
	
	private static final int PAGE_SIZE = 5;

	@Override
    public Page<Member> findAllCustomers(Integer pageNumber) {
        PageRequest request =
            new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
        return memberRepo.findAll(request);
    }

	public List<Member> getAll() {
		return memberRepo.findAll();
	}
	
	public Member saveAndFlush(Member customer) {
		return memberRepo.saveAndFlush(customer);
	}
	
//	public Member getMemberByName(String name) {
//		return memberRepo.findOneByName(name);
//	}
//	
	@Override
	public Member getMemberById(Integer id) {
		return memberRepo.findById(id);
	}
	@Override
	public Member getMemberByEmail(String email) {
		return memberRepo.findOneByEmail(email);
	}

	
	@Override
	public Member saveNewCustomer(Member member) {
		
		String password = member.getPassword();
		String encodedPw = passwordEncoder.encode(password);		
		member.setPassword(encodedPw);
		
		member.setUsername(member.getEmail());
		member = memberRepo.saveAndFlush(member);
		return member;
	}

	public boolean validatePassword(String rawPassword, String encodeddPassword) {

		return (passwordEncoder.matches(rawPassword, encodeddPassword));
	}

	public boolean checkEmailExists(String email) {
		Member cust = new Member();
		cust = memberRepo.findOneByEmail(email);
		if (cust != null && cust.getId() != null)
			return true;
		else return false;
	}

	@Override
	public Member registerNewMemberAccount(MemberDto accountDto)
			throws EmailExistsException {
        LOGGER.info("in registerNewMemberAccount");
        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException("There is an account with that email adress: " + accountDto.getEmail());
        }
        final Member user = new Member();

        user.setName(accountDto.getName());
        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setAddress(accountDto.getAddress());
        user.setPhone(accountDto.getPhone());
        user.setCityRegion(accountDto.getCityRegion());
        user.setEmail(accountDto.getEmail());
        Role userRole = roleRepo.findByName("USER");
        user.setRole(userRole);
        user.setEnabled(true);

//        user.setRoles(Arrays.asList(roleRepo.findByName("ROLE_USER")));
        Member newMember = memberRepo.save(user);
        LOGGER.info("new member: " + newMember);
        return newMember;
	}

	@Override
	public void deleteMember(Member member) {
        memberRepo.delete(member);
	}

	@Override
	public void changeMemberPassword(Member Member, String password) {
		Member.setPassword(passwordEncoder.encode(password));
        memberRepo.save(Member);
	}

    @Override
    public boolean checkIfValidOldPassword(final Member member, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, member.getPassword());
    }
	
    private boolean emailExist(final String email) {
        final Member member = memberRepo.findOneByEmail(email);
        if (member != null) {
            return true;
        }
        return false;
    }

}
