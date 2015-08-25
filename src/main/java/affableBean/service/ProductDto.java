package affableBean.service;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import affableBean.domain.Category;
import affableBean.domain.Product;
import affableBean.repository.CategoryRepository;
import affableBean.repository.ProductRepository;


public class ProductDto {
	
	public ProductDto() {
		super();
	}
	public ProductDto(String name, String description, BigDecimal price,
			String categoryName) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.categoryName = categoryName;
//		this.imagePath = imagePath;
	}

	public ProductDto(Product product) {
		super();
		this.setId(product.getId());
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.categoryName = product.getCategory().getName();
	}
	private Integer id;
	
		@NotNull
	    @Size(min = 1)
	    private String name;

	    @NotNull
	    @Size(min = 1)
	    private String description;

	    private BigDecimal price;

	    @NotNull
	    @Size(min = 1)
	    private String categoryName;
	    
	    private MultipartFile picture;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((categoryName == null) ? 0 : categoryName.hashCode());
			result = prime * result
					+ ((description == null) ? 0 : description.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((price == null) ? 0 : price.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProductDto other = (ProductDto) obj;
			if (categoryName == null) {
				if (other.categoryName != null)
					return false;
			} else if (!categoryName.equals(other.categoryName))
				return false;
			if (description == null) {
				if (other.description != null)
					return false;
			} else if (!description.equals(other.description))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (price == null) {
				if (other.price != null)
					return false;
			} else if (!price.equals(other.price))
				return false;
			return true;
		}
		public MultipartFile getPicture() {
			return picture;
		}
		public void setPicture(MultipartFile picture) {
			this.picture = picture;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
	    

}
