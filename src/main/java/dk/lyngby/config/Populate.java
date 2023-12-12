package dk.lyngby.config;


import dk.lyngby.model.*;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Set<Room> calRooms = getCalRooms();
        Set<Room> hilRooms = getHilRooms();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel california = new Hotel("Hotel California", "California", Hotel.HotelType.LUXURY);
            Hotel hilton = new Hotel("Hilton", "Copenhagen", Hotel.HotelType.STANDARD);
            Picture picture = new Picture("https://images.unsplash.com/photo-1701906268416-b461ec4caa34?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHwyfHx8fHx8Mnx8MTcwMjI5MzE1Mnw&ixlib=rb-4.0.3&q=80&w=400", "the sun is setting over the clouds in the sky");
            User admin1 = new User("admin1", "admin1");
            Role admin = new Role("admin");
            admin1.addRole(admin);
            em.persist(admin);
            em.persist(admin1);
            em.persist(picture);
            california.setRooms(calRooms);
            hilton.setRooms(hilRooms);
            em.persist(california);
            em.persist(hilton);
            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Room> getCalRooms() {
        Room r100 = new Room(100, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r101 = new Room(101, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r102 = new Room(102, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r103 = new Room(103, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r104 = new Room(104, new BigDecimal(3200), Room.RoomType.DOUBLE);
        Room r105 = new Room(105, new BigDecimal(4500), Room.RoomType.SUITE);

        Room[] roomArray = {r100, r101, r102, r103, r104, r105};
        return Set.of(roomArray);
    }

    @NotNull
    private static Set<Room> getHilRooms() {
        Room r111 = new Room(111, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r112 = new Room(112, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r113 = new Room(113, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r114 = new Room(114, new BigDecimal(2520), Room.RoomType.DOUBLE);
        Room r115 = new Room(115, new BigDecimal(3200), Room.RoomType.DOUBLE);
        Room r116 = new Room(116, new BigDecimal(4500), Room.RoomType.SUITE);

        Room[] roomArray = {r111, r112, r113, r114, r115, r116};
        return Set.of(roomArray);
    }
}
