package affableBean.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import affableBean.domain.Member;
import affableBean.domain.PaymentInfo;
import affableBean.repository.MemberRepository;
import affableBean.repository.PaymentInfoRepository;

@Service
@Transactional
public class MemberService {

	@Autowired
	private MemberRepository memberRepo;
	
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	
	private static final int PAGE_SIZE = 5;

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
	
	public Member findOneByName(String name) {
		return memberRepo.findOneByName(name);
	
	}
	
	public Member findById(Integer id) {
		return memberRepo.findById(id);
	}
	
	public Member saveNewCustomer(Member member) {
		
		String password = member.getPassword();
		
		
		String encodedPw = encoder.encode(password);
		
		member.setPassword(encodedPw);
		
		
		member = memberRepo.saveAndFlush(member);
		return member;
	}

	public boolean validatePassword(String rawPassword, String encodeddPassword) {

		return (encoder.matches(rawPassword, encodeddPassword));
	}

	public boolean checkEmailExists(String email) {
		Member cust = new Member();
		cust = memberRepo.findOneByEmail(email);
		if (cust != null && cust.getId() != null)
			return true;
		else return false;
	}
	
}
