package dk.lyngby.config;


import dk.lyngby.model.Picture;
import jakarta.persistence.EntityManagerFactory;


public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM Picture").executeUpdate();
            
            Picture download1 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_619_71/1km_6194_714/2021_84_40_2_0031_00070238.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download2 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_619_71/1km_6194_714/2021_84_40_3_0031_00070145.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download3 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_619_71/1km_6194_714/2021_84_40_4_0028_00061230.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download4 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_619_71/1km_6194_714/2021_84_40_5_0034_00074947.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download5 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_619_71/1km_6194_714/2021_84_40_1_0031_00070212.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download6 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_628_55/1km_6287_556/2021_81_05_2_0096_00000801.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download7 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_628_55/1km_6287_556/2021_81_05_3_0096_00000821.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download8 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_628_55/1km_6287_556/2021_81_05_4_0094_00001510.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download9 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_628_55/1km_6287_556/2021_81_05_5_0099_00004247.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            Picture download10 = new Picture("https://cdn.dataforsyningen.dk/skraafoto_server/COG_oblique_2021/10km_628_55/1km_6287_556/2021_81_05_1_0096_00000811.tif?token=c47b1ef9a9c7dc1d11a154d4e21739f0");
            em.persist(download1);
            em.persist(download2);
            em.persist(download3);
            em.persist(download4);
            em.persist(download5);
            em.persist(download6);
            em.persist(download7);
            em.persist(download8);
            em.persist(download9);
            em.persist(download10);
            em.getTransaction().commit();
        }
    }
}
