package affableBean.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import affableBean.validation.PasswordMatches;
import affableBean.validation.ValidPassword;

@PasswordMatches
public class PasswordDto {

    private String oldPassword;
    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((matchingPassword == null) ? 0 : matchingPassword.hashCode());
		result = prime * result
				+ ((oldPassword == null) ? 0 : oldPassword.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
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
		PasswordDto other = (PasswordDto) obj;
		if (matchingPassword == null) {
			if (other.matchingPassword != null)
				return false;
		} else if (!matchingPassword.equals(other.matchingPassword))
			return false;
		if (oldPassword == null) {
			if (other.oldPassword != null)
				return false;
		} else if (!oldPassword.equals(other.oldPassword))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PasswordDto [oldPassword=" + oldPassword + ", password="
				+ password + ", matchingPassword=" + matchingPassword + "]";
	}
}
