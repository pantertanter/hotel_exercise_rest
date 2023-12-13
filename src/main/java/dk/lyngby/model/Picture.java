package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "picture_url", nullable = false, unique = true)
    private String url;

    @Setter
    @Column(name = "picture_alt")
    private String alt;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    private User user;


    public Picture(String url, String alt) {
        this.url = url;
        this.alt = alt;
    }
}
