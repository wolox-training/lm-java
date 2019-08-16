package wolox.training.models;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import wolox.training.utils.MessageConstants;

@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

    public Privilege(String name) {
        this.name = name;
        this.roles = new HashSet<>();
    }

    public Privilege() {
        this.roles = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoles(Set<Role> roles) {
        Preconditions.checkNotNull(roles, MessageConstants.NULL_PARAMETER);
        this.roles = roles;
    }
}
