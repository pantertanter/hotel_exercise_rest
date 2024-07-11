package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // Add Setter at the class level to generate setters for all fields
@NoArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "picture_alt", referencedColumnName = "picture_alt")
    private Picture picture;

    @ManyToOne
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    private User user;

    // Constructor with rating parameter
    public Rating(Integer rating) {
        this.rating = rating;
    }

    public Rating(Integer rating, User user) {
        this.rating = rating;
        this.user = user;
    }

    // Optional: Full argument constructor
    public Rating(Integer rating, Picture picture, User user) {
        this.rating = rating;
        this.picture = picture;
        this.user = user;
    }
}
