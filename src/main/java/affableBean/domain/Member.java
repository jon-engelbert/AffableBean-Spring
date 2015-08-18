package affableBean.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Member implements java.io.Serializable {

	private static final long serialVersionUID = 3394395703589149580L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "firstname", nullable = false, length = 45)
	private String firstName;
	@Column(name = "lastname", nullable = false, length = 45)
	private String lastName;
	private String email;
	@Column(name = "username", unique = true, nullable = false, length = 45)
	private String username;
	@Column(length = 60)
	private String password;
	private boolean enabled;
	@Column(name="tokenexpired")
	private boolean tokenExpired;
	@JoinTable(name = "member_role", joinColumns = @JoinColumn(name = "member_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@ManyToMany
	private Set<Role> roles  = new HashSet<Role>();

	public Member(String firstName, String lastName, String email,
			String username, String password, boolean enabled,
			boolean tokenExpired) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.setTokenExpired(tokenExpired);
		// this.roles.add(role);
	}

	public Member() {
		super();
		this.enabled = false;
		this.setTokenExpired(false);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(final Set<Role> roles) {
		this.roles = roles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		Member other = (Member) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	public boolean isTokenExpired() {
		return tokenExpired;
	}

	public void setTokenExpired(boolean tokenExpired) {
		this.tokenExpired = tokenExpired;
	}

}
