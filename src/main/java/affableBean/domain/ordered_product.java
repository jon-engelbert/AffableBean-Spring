package affableBean.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ordered_product {
	protected ordered_product() {
	};

	public ordered_product(int customer_order_id, int product_id, int quantity) {
		super();
		this.customer_order_id = customer_order_id;
		this.product_id = product_id;
		this.quantity = quantity;
	}

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
	@JoinColumn(name = "product")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name= "customer_order")
	private Customer_order customer_order;
}
