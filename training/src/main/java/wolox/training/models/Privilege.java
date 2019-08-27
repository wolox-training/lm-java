package wolox.training.models;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.thymeleaf.util.StringUtils;
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

    public Privilege() {
        this.roles = new HashSet<>();
    }

    public Privilege(String name) {
        this.roles = new HashSet<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!StringUtils.isEmpty(name), MessageConstants.EMPTY_STRING);
        this.name = name;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void setRoles(HashSet<Role> roles) {
        Preconditions.checkNotNull(roles);
        this.roles = roles;
    }
}
