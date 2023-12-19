package dk.lyngby.dto;

import dk.lyngby.model.Rating;

import java.util.List;
import java.util.stream.Collectors;

public class RatingDto {

    private Long id;
    private int rating;

    public RatingDto(Rating rating) {
        this.id = rating.getId();
        this.rating = rating.getRating();
    }

    public static List<RatingDto> toRatingDTOList(List<Rating> ratings) {
        return ratings.stream().map(RatingDto::new).collect(Collectors.toList());
    }
}
