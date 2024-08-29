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
    private Integer id;

    @Setter
    @Getter
    @Column(name = "picture_url", nullable = false, unique = true)
    private String pictureUrl;

    public Picture(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
