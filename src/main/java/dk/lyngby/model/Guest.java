package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "guest")
@NamedQueries(@NamedQuery(name = "Guest.deleteAllRows", query = "DELETE from Guest"))
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id", nullable = false)
    private int guestId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "guest")
    private Set<Bookning> booknings;

    public Guest(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(lastName, guest.lastName) && Objects.equals(email, guest.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, email);
    }
}
