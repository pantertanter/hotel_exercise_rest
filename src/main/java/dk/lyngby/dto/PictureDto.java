package dk.lyngby.dto;

import dk.lyngby.model.Picture;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PictureDto {

    private Integer id;
    private String pictureUrl;

    public PictureDto(Picture picture) {
        this.id = picture.getId();
        this.pictureUrl = picture.getPictureUrl();
    }

    public PictureDto(String pictureUrl)
    {
        this.pictureUrl = pictureUrl;
    }

    public static List<PictureDto> toPictureDTOList(List<Picture> Pictures) {
        return Pictures.stream().map(PictureDto::new).collect(Collectors.toList());
    }
}
