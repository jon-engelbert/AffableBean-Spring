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
public class Customer_order {
	private Customer_order() {
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "date_created")
	private Date date_created;

	@Column(name = "confirmation_number")
	private Integer confirmation_number;

	@ManyToOne
	@JoinColumn(name = "customer")
	private Customer customer;
	
	public Customer_order(Long id, Double amount, Date date_created, Integer confirmation_number) {
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
