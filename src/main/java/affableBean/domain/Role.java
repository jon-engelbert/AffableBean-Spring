package affableBean.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role", catalog = "affablebean")
public class Role implements java.io.Serializable {
	
	public Role() {}

	public Role(Byte id, String name, Set<Member> members) {
		super();
		this.id = id;
		this.name = name;
		this.members = members;
	}

	private static final long serialVersionUID = 6464512438578201997L;
	
	private Byte id;
	private String name;
	private Set<Member> members = new HashSet<Member>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Byte getId() {
		return id;
	}

	public void setId(Byte id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	};
	
}
