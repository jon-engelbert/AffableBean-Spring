package affableBean.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class CustomerOrder {
	protected CustomerOrder() {
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@Column(name = "amount")
	private Double amount;

	@Temporal(TemporalType.DATE)
	private Date date_created;
//	@Column(name = "date_created")

//	@Column(name = "confirmation_number")
	private Integer confirmation_number;

//	@JoinColumn(name = "customer")
	@ManyToOne
	private Customer customer;
	
//	@OneToMany(targetEntity=OrderedProduct.class, fetch = FetchType.EAGER, mappedBy = "CustomerOrder")
//	private List<OrderedProduct> orderedProducts;

	public CustomerOrder(Long id, Double amount, Date date_created, Integer confirmation_number) {
		this.id = id;
		this.amount = amount;
		this.date_created = date_created;
		this.confirmation_number = confirmation_number;
	}

	public Long getId() {
		return id;
	}

	public Double getAmount() {
		return amount;
	}

	public Date getDate_created() {
		return date_created;
	}

	public Integer getConfirmation_number() {
		return confirmation_number;
	}

	public Customer getCustomer() {
		return customer;
	}
}
