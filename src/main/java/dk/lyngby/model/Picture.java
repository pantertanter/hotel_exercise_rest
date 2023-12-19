package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "picture")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_id", nullable = false, unique = true)
    private Long id;

    @Setter
    @Column(name = "picture_url", nullable = false)
    private String url;

    @Setter
    @Column(name = "picture_alt")
    private String alt;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    private User user;

    @Setter
    @Getter
    @OneToMany(mappedBy = "picture")
    private List<Rating> ratings;

    public Picture(String url, String alt) {
        this.url = url;
        this.alt = alt;
        this.ratings = new ArrayList<>(); // Initialize ratings list
    }

    public void addRating(Rating rating) {
        if (ratings == null) {
            ratings = new ArrayList<>(); // Initialize ratings list if null
        }
        ratings.add(rating);
    }
}
