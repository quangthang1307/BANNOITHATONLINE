package poly.edu.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleId;

    @Column(name = "Rolename")
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<AccountRole> roleAccount;

    public Role() {
    }

    public Role(Integer roleId, String roleName, Set<AccountRole> roleAccount) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleAccount = roleAccount;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<AccountRole> getRoleAccount() {
        return roleAccount;
    }

    public void setRoleAccount(Set<AccountRole> roleAccount) {
        this.roleAccount = roleAccount;
    }

}
