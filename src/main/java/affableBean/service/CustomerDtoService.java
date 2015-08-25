package affableBean.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import affableBean.domain.Customer;
import affableBean.repository.CustomerRepository;

@Service
@Transactional
public class CustomerDtoService {
    @Autowired
    private CustomerRepository repository;

    public Customer addNewCustomer(CustomerDto customerDto)  {
        final Customer customer = new Customer();

        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setCityRegion(customerDto.getCityRegion());
        customer.setCcNumber(customerDto.getCcNumber());
        customer.setPassword(customerDto.getPassword());

        return repository.save(customer);
    }

    public Customer editCustomer(CustomerDto customerDto)  {
    	final Customer customer = repository.findOneByName(customerDto.getName());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setCityRegion(customerDto.getCityRegion());
        customer.setCcNumber(customerDto.getCcNumber());
        customer.setPassword(customerDto.getPassword());

        return repository.save(customer);
    }
}
