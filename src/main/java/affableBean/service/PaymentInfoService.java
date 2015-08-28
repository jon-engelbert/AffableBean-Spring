package affableBean.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import affableBean.domain.PaymentInfo;
import affableBean.repository.PaymentInfoRepository;

@Service
@Transactional
public class PaymentInfoService {
	@Autowired
	private PaymentInfoRepository paymentInfoRepo;
	
    public PaymentInfo editPaymentInfo(PaymentInfo paymentInfo)  {
    	final PaymentInfo existingPaymentInfo = paymentInfoRepo.findOneById(paymentInfo.getId());
    	if (existingPaymentInfo == null) {
    		return addPaymentInfo(paymentInfo);
    	}
    	existingPaymentInfo.setName(paymentInfo.getName());
//        customer.setEmail(paymentInfo.getEmail());
//        customer.setPhone(paymentInfo.getPhone());
    	existingPaymentInfo.setAddress(paymentInfo.getAddress());
    	existingPaymentInfo.setCityRegion(paymentInfo.getCityRegion());
    	existingPaymentInfo.setCcNumber(paymentInfo.getCcNumber());

        return paymentInfoRepo.save(existingPaymentInfo);
    }

    public PaymentInfo addPaymentInfo(PaymentInfo newPaymentInfo) {
        return paymentInfoRepo.saveAndFlush(newPaymentInfo);
	}

}
