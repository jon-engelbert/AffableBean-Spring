package affableBean.service;

import java.math.BigDecimal;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import affableBean.domain.Category;
import affableBean.domain.Product;
import affableBean.repository.CategoryRepository;
import affableBean.repository.ProductRepository;

@Service
@Transactional
public class ProductDtoService {
	@Autowired
	private CategoryRepository categoryRepo;
    @Autowired
    private ProductRepository repository;
	

	    
    public Product addNewProduct(ProductDto productDto)  {
    	String catName;
        final Product product = new Product();

        product.setName(productDto.getName());
        catName = productDto.getCategoryName();
        product.setDescription(productDto.getDescription());
        System.out.println("catName: " +catName);
        System.out.println("categoryRepo: " +categoryRepo);
        Category cat = categoryRepo.findOneByName(catName);
        System.out.println("category: " +cat);
        product.setCategory(cat);
        product.setPrice(productDto.getPrice());

        return repository.save(product);
    }

    public Product editProduct(ProductDto productDto)  {
    	String catName;
        final Product product = repository.findOneByName(productDto.getName());
        catName = productDto.getCategoryName();
        product.setDescription(productDto.getDescription());
        System.out.println("catName: " +catName);
        System.out.println("categoryRepo: " +categoryRepo);
        Category cat = categoryRepo.findOneByName(catName);
        System.out.println("category: " +cat);
        product.setCategory(cat);
        product.setPrice(productDto.getPrice());

        return repository.save(product);
    }

}
