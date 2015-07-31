package affableBean.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Product {
	public Product(Long id, String name, Double price, String description,
			Date last_update) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
		this.last_update = last_update;
	}

	protected Product() {
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@Column(name = "name")
	private String name;

//	@Column(name = "price")
	private Double price;

//	@Column(name = "description")
	private String description;

//	@Column(name = "last_update")
	private Date last_update;
	
//	@Column(name="categoryId")
	@ManyToOne()
//	@JoinColumn(name = "products")
	private Category category;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public Date getLast_update() {
		return last_update;
	}

}
