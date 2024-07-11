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
    @Setter
    @Column(name = "picture_alt")
    private String alt;

    @Setter
    @Column(name = "picture_url", nullable = false)
    private String url;

    @Setter
    @Column(name = "photographer_name")
    private String pName;

    @Setter
    @Column(name = "photographer_userName")
    private String pUserName;

    @Setter
    @Column(name = "p_user_link")
    private String pUserLink;

    @Setter
    @Column(name = "p_down_link")
    private String pDownLink;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    private User user;

    @Setter
    @Getter
    @OneToMany(mappedBy = "picture")
    private List<Rating> ratings;

    public Picture(String url, String alt, String pName, String pUserName, String pUserLink, String pDownLink) {
        this.url = url;
        this.alt = alt;
        this.pName = pName;
        this.pUserName = pUserName;
        this.pUserLink = pUserLink;
        this.pDownLink = pDownLink;
        this.ratings = new ArrayList<>(); // Initialize ratings list
    }

    public void addRating(Rating rating) {
        if (ratings == null) {
            ratings = new ArrayList<>(); // Initialize ratings list if null
        }
        ratings.add(rating);
        rating.setPicture(this); // Set the Picture instance in the Rating entity
    }
}
