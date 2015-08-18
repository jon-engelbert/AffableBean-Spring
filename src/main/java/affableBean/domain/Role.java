package affableBean.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 6464512438578201997L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", unique = true, nullable = false)
	private Byte id;
	@Column(name = "name", nullable = false, length = 45)
	private String name;
//	@JoinTable(name = "member_role", joinColumns = @JoinColumn(name = "member_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ManyToMany(mappedBy="roles")
	private Set<Member> members = new HashSet<Member>();

	// @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	// private Collection<MemberRole> memberRolesCollection;
	// private Collection<Member> members;

	// @ManyToMany
	// @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name =
	// "role_id", referencedColumnName = "id") , inverseJoinColumns =
	// @JoinColumn(name = "privilege_id", referencedColumnName = "id") )
	// private Collection<Privilege> privileges;

	public Role() {
	}

	public Role(Byte id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Byte getId() {
		return id;
	}

	public void setId(Byte id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
