package dk.lyngby.dto;

import dk.lyngby.model.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RoomDto {
    private Integer roomNumber;
    private Integer hotelId;
    private Integer price;

    public RoomDto(Room room) {
        this.roomNumber = room.getRoomNumber();
        this.price = room.getRoomPrice().intValue();
        this.hotelId = room.getHotel().getId() != null ? room.getHotel().getId() : null;
    }

    public static List<RoomDto> toRoomDTOList(List<Room> rooms) {
        return List.of(rooms.stream().map(RoomDto::new).toArray(RoomDto[]::new));
    }
}
