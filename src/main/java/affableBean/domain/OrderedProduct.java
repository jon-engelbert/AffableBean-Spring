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

	public OrderedProduct(int customer_order_id, int product_id, int quantity) {
		super();
		this.customer_order_id = customer_order_id;
		this.product_id = product_id;
		this.quantity = quantity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int customer_order_id;
	private int product_id;
	private int quantity;

	public int getCustomer_order_id() {
		return customer_order_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public int getQuantity() {
		return quantity;
	}
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
	private CustomerOrder customerOrder;
}
