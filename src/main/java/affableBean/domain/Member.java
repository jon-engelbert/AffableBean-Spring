package affableBean.domain;

import javax.persistence.*;

@Entity
@Table(name = "member", catalog = "test", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Member implements java.io.Serializable {

	public Member(Role role, String name, String username, String password,
			byte status) {
		super();
		this.role = role;
		this.name = name;
		this.username = username;
		this.password = password;
		this.status = status;
	}

	private static final long serialVersionUID = 3394395703589149580L;

	private Integer id;
	private Role role;
	private String name;
	private String username;
	private String password;
	private String hash;
	private byte status;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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
	
	public String getHash() {
		return this.hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
