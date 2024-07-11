package dk.lyngby.dto;

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
    private String pName;
    private String pUserName;
    private String pUserLink;
    private String pDownLink;

    public PictureDto(Picture picture) {
        this.url = picture.getUrl();
        this.alt = picture.getAlt();
        this.pName = picture.getPName();
        this.pUserName = picture.getPUserName();
        this.pUserLink = picture.getPUserLink();
        this.pDownLink = picture.getPDownLink();
    }

    public static List<PictureDto> toPictureDTOList(List<Picture> pictures) {
        return pictures.stream().map(PictureDto::new).collect(Collectors.toList());
    }
}
