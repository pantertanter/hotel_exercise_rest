package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "bookning")
@NamedQueries(@NamedQuery(name = "Bookning.deleteAllRows", query = "DELETE from Bookning"))
public class Bookning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookning_id", nullable = false)
    private int bookningId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @ManyToMany
    @JoinTable(name = "bookning_room",
            joinColumns = @JoinColumn(name = "bookning_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Set<Room> rooms;



    @PrePersist
    public void prePersist() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDate.now();
    }


    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }
}
