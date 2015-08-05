package affableBean.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import affableBean.domain.Category;
import affableBean.repository.CategoryRepository;
import affableBean.repository.CustomerRepository;

@Service
@Transactional
public class CategoryService   {
	@Resource
	private CategoryRepository categoryRepo;

	public long count() {
		return categoryRepo.count();
	}

	public void delete(Category arg0) {
		categoryRepo.delete(arg0);
	}

	public void delete(Integer arg0) {
		categoryRepo.delete(arg0);
	}

	public void deleteAll() {
		categoryRepo.deleteAll();
	}

	public boolean exists(Integer arg0) {
		return categoryRepo.exists(arg0);
	}

	public List<Category> findAll() {
		return categoryRepo.findAll();
	}

	public Category findById(Integer id) {
		return categoryRepo.findById(id);
	}

	public Category findOne(Integer arg0) {
		return categoryRepo.findOne(arg0);
	}

	public Category findOneByName(String name) {
		return categoryRepo.findOneByName(name);
	}

	public void flush() {
		categoryRepo.flush();
	}

	public <S extends Category> S save(S arg0) {
		return categoryRepo.save(arg0);
	}

	public <S extends Category> S saveAndFlush(S entity) {
		return categoryRepo.saveAndFlush(entity);
	}

	
	
}
