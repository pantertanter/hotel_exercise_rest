package dk.lyngby.dto;

import dk.lyngby.model.Rating;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class RatingDto {

    private int rating;

    public RatingDto(Rating rating) {
        this.rating = rating.getRating();
    }

    public static List<RatingDto> toRatingDTOList(List<Rating> ratings) {
        return ratings.stream().map(RatingDto::new).collect(Collectors.toList());
    }
}
