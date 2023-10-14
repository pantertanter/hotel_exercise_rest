package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private Set<Role> roleList = new HashSet<>();

    public User(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = BCrypt.hashpw(userPassword, BCrypt.gensalt());
    }

    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, userPassword);
    }

    public void setRoleList(Set<Role> roleList) {
        if(roleList == null) return;
        roleList.forEach(role -> role.getUserList().add(this));
        this.roleList = roleList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
