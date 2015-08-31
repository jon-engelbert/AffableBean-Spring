package affableBean.service;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import affableBean.domain.Member;
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
		this.id = member.getId();
		this.password = member.getPassword();
	}
    
	private Integer id;
    private String phone;
    private String address;
    private String cityRegion;

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

    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(final Integer role) {
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
}
