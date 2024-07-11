package dk.lyngby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
@NamedQueries(@NamedQuery(name = "User.deleteAllRows", query = "DELETE from User"))
@Getter
@NoArgsConstructor
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "user_name", length = 25)
    private String username;

    @Basic(optional = false)
    @Column(name = "user_password", length = 255, nullable = false)
    private String userPassword;

    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany(fetch = FetchType.EAGER)
    @Setter
    private Set<Role> roleList = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Picture> pictures = new HashSet<>();

    @JsonIgnore
    @Setter
    @Getter
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Rating> ratings;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String userPassword) {
        this.username = username;
        this.userPassword = BCrypt.hashpw(userPassword, BCrypt.gensalt());
    }

    public Set<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        Set<String> rolesAsStrings = new LinkedHashSet<>();
        roleList.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, userPassword);
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = BCrypt.hashpw(userPassword, BCrypt.gensalt());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
        picture.setUser(this);
    }

    public void addRating(Rating rating) {
        if (ratings == null) {
            ratings = new ArrayList<>(); // Initialize ratings list if null
        }
        ratings.add(rating);
        rating.setUser(this); // Set the Picture instance in the Rating entity
    }

    public void removePicture(Picture picture) {
        pictures.remove(picture);
        picture.setUser(null);
    }
}