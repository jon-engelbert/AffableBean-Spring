///*
// * AffableBean demo
// */
//
//package affableBean.domain;
//
//import java.io.Serializable;
//
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
//
//@Embeddable
//public class MemberRolePK implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	@Basic(optional = false)
//	@Column(name = "member_id")
//	private int memberId;
//	@Basic(optional = false)
//	@Column(name = "role_id")
//	private int roleId;
//
//	public MemberRolePK() {
//	}
//
//	public MemberRolePK(int memberId, int roleId) {
//		this.memberId = memberId;
//		this.roleId = roleId;
//	}
//
//	public int getMemberId() {
//		return memberId;
//	}
//
//	public void setMemberId(int memberId) {
//		this.memberId = memberId;
//	}
//
//	public int getRoleId() {
//		return roleId;
//	}
//
//	public void setRoleId(int roleId) {
//		this.roleId = roleId;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + memberId;
//		result = prime * result + roleId;
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		MemberRolePK other = (MemberRolePK) obj;
//		if (memberId != other.memberId)
//			return false;
//		if (roleId != other.roleId)
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "MemberRolePK [memberId=" + memberId + ", roleId=" + roleId
//				+ "]";
//	}
//
//
//}