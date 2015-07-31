package affableBean.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Customer {
	protected Customer() {
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@Column(name = "name")
	private String name;

//	@Column(name = "email")
	private String email;

//	@Column(name = "phone")
	private String phone;

//	@Column(name = "address")
	private String address;

//	@Column(name = "city_region")
	private String city_region;

//	@Column(name = "cc")
	private String cc;

//	@OneToMany(targetEntity = CustomerOrder.class, fetch = FetchType.EAGER, mappedBy = "Customer")
//	private List<CustomerOrder> orders;

	public Customer(Long id, String name, String email, String phone,
			String address, String city_region, String cc) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.city_region = city_region;
		this.cc = cc;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getCity_region() {
		return city_region;
	}

	public String getCc() {
		return cc;
	}

//	public List<CustomerOrder> getOrders() {
//		return orders;
//	}

}
