package affableBean.service;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import affableBean.domain.Member;
import affableBean.domain.Role;
import affableBean.validation.PasswordMatches;
import affableBean.validation.ValidEmail;
import affableBean.validation.ValidPassword;


@PasswordMatches
public class MemberDto {
    public MemberDto() {
		super();
	}

    public MemberDto(Member member) {
		super();
		this.email = member.getEmail();
		this.phone = member.getPhone();
		this.address = member.getAddress();
		this.cityRegion = member.getCityRegion();
		this.name = member.getName();
		this.username = member.getUsername();
		this.id = member.getId();
		this.password = member.getPassword();
		this.role = member.getRole();
//		this.roles = member.getRoles();
	}
    
	private Integer id;
    private String phone;
    private String address;
    private String cityRegion;
    private Role role;
//    private Collection<Role> roles;

    @NotNull
    @Size(min = 1)
    private String name;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @Size(min = 1)
    private String email;

    @NotNull
    @Size(min = 1)
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }


//  public Collection<Role> getRoles() {
//  return roles;
    public Role getRole() {
        return role;
    }

//    public void setRoles(final Collection<Role> roles) {
//        this.roles = roles;
    public void setRole(final Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
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

	@Override
	public String toString() {
		return "MemberDto [id=" + id + ", phone=" + phone + ", address="
				+ address + ", cityRegion=" + cityRegion + ", name=" + name
				+ ", password=" + password + ", matchingPassword="
				+ matchingPassword + ", email=" + email + ", username="
				+ username + ", role=" + role + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((cityRegion == null) ? 0 : cityRegion.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((matchingPassword == null) ? 0 : matchingPassword.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
//		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		MemberDto other = (MemberDto) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (cityRegion == null) {
			if (other.cityRegion != null)
				return false;
		} else if (!cityRegion.equals(other.cityRegion))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (matchingPassword == null) {
			if (other.matchingPassword != null)
				return false;
		} else if (!matchingPassword.equals(other.matchingPassword))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
//		} else if (!roles.equals(other.roles))
//			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
