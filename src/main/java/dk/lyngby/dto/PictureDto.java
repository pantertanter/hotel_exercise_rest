package dk.lyngby.dto;

import dk.lyngby.model.Hotel;
import dk.lyngby.model.Picture;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PictureDto {
    private String url;
    private String alt;

    public PictureDto(Picture picture) {
        this.url = picture.getUrl();
        this.alt = picture.getAlt();
    }

    public static List<PictureDto> toPictureDTOList(List<Picture> pictures) {
        return pictures.stream().map(PictureDto::new).collect(Collectors.toList());
    }
}
