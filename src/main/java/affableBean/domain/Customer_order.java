package affableBean.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

}
