package affableBean.domain;

import javax.persistence.*;

@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Member implements java.io.Serializable {

	public Member(String name, String username, String password,
			boolean enabled, Role role) {
		super();
		this.name = name;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.role = role;
	}

	private static final long serialVersionUID = 3394395703589149580L;

	private Integer id;
	private String name;
	private String username;
	private String password;
	private boolean enabled;
	private Role role;

	public Member() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
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

	@Column(name = "username", unique = true, nullable = false, length = 45)
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
