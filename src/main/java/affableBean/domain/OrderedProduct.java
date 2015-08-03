package affableBean.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderedProduct {
	protected OrderedProduct() {
	};

	public OrderedProduct(CustomerOrder customerOrder, Product product, int quantity) {
		super();
		this.customerOrder = customerOrder;
		this.product = product;
		this.quantity = quantity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Product product;
	@JoinColumn(name = "customerorder_id", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private CustomerOrder customerOrder;
	private int quantity;

	public Product getProduct() {
		return product;
		}

		public void setProduct(Product product) {
		this.product = product;
		}
		
		public CustomerOrder getCustomerOrder() {
		return customerOrder;
		}
		 
		public void setCustomerOrder(CustomerOrder customerOrder) {
		this.customerOrder = customerOrder;
		}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
