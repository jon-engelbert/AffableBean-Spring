package affableBean.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import affableBean.domain.Member;
import affableBean.domain.PaymentInfo;
import affableBean.repository.PaymentInfoRepository;
import affableBean.repository.MemberRepository;

@Service
@Transactional
public class CustomerDtoService {
    @Autowired
    private MemberRepository memberRepo;

    public Member addNewCustomer(CustomerDto customerDto)  {
        final Member member = new Member();
        final PaymentInfo paymentInfo = new PaymentInfo();

        member.setName(customerDto.getName());
        member.setEmail(customerDto.getEmail());
        member.setPhone(customerDto.getPhone());
        member.setAddress(customerDto.getAddress());
        member.setCityRegion(customerDto.getCityRegion());
        member.setPassword(customerDto.getPassword());

        Member savedMember = memberRepo.save(member);
        paymentInfo.setCcNumber(customerDto.getCcNumber());
        paymentInfo.setMember(savedMember);
        return savedMember;

    }

    public Member editCustomer(CustomerDto customerDto)  {
    	final Member customer = memberRepo.findOneByName(customerDto.getName());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setCityRegion(customerDto.getCityRegion());
        customer.setPassword(customerDto.getPassword());

        Member savedMember = memberRepo.save(customer);

        final PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCcNumber(customerDto.getCcNumber());
        paymentInfo.setMember(savedMember);
        return savedMember;
    }
}
