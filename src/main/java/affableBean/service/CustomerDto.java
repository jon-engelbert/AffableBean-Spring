package affableBean.service;

import affableBean.domain.Customer;


public class CustomerDto {
    public CustomerDto(Integer id, String name, String email, String phone,
			String address, String cityRegion, String ccNumber, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.cityRegion = cityRegion;
		this.ccNumber = ccNumber;
		this.password = password;
		this.confPassword = password;
	}
    
    public CustomerDto(Customer cust) {
		super();
		this.id = cust.getId();
		this.name = cust.getName();
		this.email = cust.getEmail();
		this.phone = cust.getPhone();
		this.address = cust.getAddress();
		this.cityRegion = cust.getCityRegion();
		this.ccNumber = cust.getCcNumber();
		this.password = cust.getPassword();
		this.confPassword = cust.getPassword();
	}

	public CustomerDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	private Integer id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String cityRegion;
    private String ccNumber;
    private String password;
    private String confPassword;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCityRegion() {
		return cityRegion;
	}
	public void setCityRegion(String cityRegion) {
		this.cityRegion = cityRegion;
	}
	public String getCcNumber() {
		return ccNumber;
	}
	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		CustomerDto other = (CustomerDto) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CustomerDto [id=" + id + ", name=" + name + ", email=" + email
				+ ", phone=" + phone + ", address=" + address + ", cityRegion="
				+ cityRegion + ", ccNumber=" + ccNumber + ", password="
				+ password + "]";
	}

	public String getConfPassword() {
		return confPassword;
	}

	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}

}
