package affableBean.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import affableBean.domain.Product;
import affableBean.repository.ProductRepository;

@Service
@Transactional
public class ProductService  {

	@Resource
	private ProductRepository productRepo;

	
	
	public List<Product> getByCategoryId(Long id) {
		return productRepo.findByCategoryId(id);
	}
	
	public Product getById(Long id) {
		return productRepo.findById(id);
	}
	
	public Product saveAndFlush(Product product) {
		return productRepo.saveAndFlush(product);
	}
	
	public Product findById(Long id) {
		return productRepo.findById(id);
	}
	
	public Product findOneByName(String name) {
		return productRepo.findOneByName(name);
	}
	
}

