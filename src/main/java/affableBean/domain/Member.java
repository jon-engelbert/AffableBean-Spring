package affableBean.domain;

import java.util.Collection;

import javax.persistence.*;


@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Member implements java.io.Serializable {

	private static final long serialVersionUID = 3394395703589149580L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
    @Basic(optional = true)
    @Column(name = "name")
	private String name;
	@Column(name = "username", unique = true, nullable = false, length = 45)
	private String username;
    @Basic(optional = false)
    @Column(name = "password")
	private String password;
	private boolean enabled;
	
//  @JoinTable(name = "role_members", joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id") , inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") )
//    @ManyToMany(mappedBy = "members")
//    private Collection<Role> roles;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;
    @Basic(optional = true)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = true)
    @Column(name = "address")
    private String address;
    @Basic(optional = true)
    @Column(name = "city_region")
    private String cityRegion;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Collection<PaymentInfo> paymentInfoCollection;

	public Member() {
	}

	public Member(String name, String username, String email, String password,
			boolean enabled, Role role) {	// Collection<Role> roles) {
		super();
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.role = role;
//		this.roles = roles;
	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<PaymentInfo> getPaymentInfoCollection() {
		return paymentInfoCollection;
	}

	public void setPaymentInfoCollection(
			Collection<PaymentInfo> paymentInfoCollection) {
		this.paymentInfoCollection = paymentInfoCollection;
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", name=" + name + ", username=" + username
				+ ", password=" + password + ", enabled=" + enabled + ", phone=" + phone + ", address=" + address
				+ ", cityRegion=" + cityRegion + ", email=" + email
				+ ", paymentInfoCollection=" + paymentInfoCollection + "]";
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

//	public Collection<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Collection<Role> roles) {
//		this.roles = roles;
//	}
	
}
