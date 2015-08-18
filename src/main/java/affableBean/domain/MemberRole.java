//package affableBean.domain;
//
//import java.io.Serializable;
//
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//
//public class MemberRole implements Serializable {
//
//	public MemberRole() {
//		super();
//	}
//	public MemberRole(MemberRolePK orderedProductPK) {
//		super();
//		this.orderedProductPK = orderedProductPK;
//	}
//	public MemberRole(Member member, Role role) {
//		super();
//		this.member = member;
//		this.role = role;
//	}
//	private static final long serialVersionUID = 1L;
//
//	@EmbeddedId
//	protected MemberRolePK orderedProductPK;
//	@Basic(optional = false)
//	@JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
//	@ManyToOne(optional = false)
//	private Member member;
//	@JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
//	@ManyToOne(optional = false)
//	private Role role;
//	public Member getMember() {
//		return member;
//	}
//	public void setMember(Member member) {
//		this.member = member;
//	}
//	public Role getRole() {
//		return role;
//	}
//	public void setRole(Role role) {
//		this.role = role;
//	}
//	public MemberRolePK getOrderedProductPK() {
//		return orderedProductPK;
//	}
//	public void setOrderedProductPK(MemberRolePK orderedProductPK) {
//		this.orderedProductPK = orderedProductPK;
//	}
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime
//				* result
//				+ ((orderedProductPK == null) ? 0 : orderedProductPK.hashCode());
//		return result;
//	}
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		MemberRole other = (MemberRole) obj;
//		if (orderedProductPK == null) {
//			if (other.orderedProductPK != null)
//				return false;
//		} else if (!orderedProductPK.equals(other.orderedProductPK))
//			return false;
//		return true;
//	}
//	@Override
//	public String toString() {
//		return "MemberRole [orderedProductPK=" + orderedProductPK + ", member="
//				+ member + ", role=" + role + "]";
//	}
//	
//}