package dk.lyngby.controller.impl;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.IController;
import dk.lyngby.dao.impl.PictureDao;
import dk.lyngby.dto.HotelDto;
import dk.lyngby.dto.PictureDto;
import dk.lyngby.model.Picture;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PictureController implements IController<Picture, Integer> {

    private final PictureDao dao;

    public PictureController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = PictureDao.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Picture picture = dao.read(id);
        // dto
        PictureDto pictureDto = new PictureDto(picture);
        // response
        ctx.res().setStatus(200);
        ctx.json(pictureDto, HotelDto.class);
    }

    @Override
    public void readAll(Context ctx) {
        // entity
        List<Picture> pictures = dao.readAll();
        // dto
        List<PictureDto> pictureDtos = PictureDto.toPictureDTOList(pictures);
        // response
        ctx.res().setStatus(200);
        ctx.json(pictureDtos, PictureDto.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        //Hotel jsonRequest = validateEntity(ctx);
        Picture jsonRequest = ctx.bodyAsClass(Picture.class);
        // entity
        Picture picture = dao.create(jsonRequest);
        // dto
        PictureDto pictureDto = new PictureDto(picture);
        // response
        ctx.res().setStatus(201);
        ctx.json(pictureDto, PictureDto.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Picture update = dao.update(id, validateEntity(ctx));
        // dto
        PictureDto pictureDto = new PictureDto(update);
        // response
        ctx.res().setStatus(200);
        ctx.json(pictureDto, Picture.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) { return dao.validatePrimaryKey(integer); }

    @Override
    public Picture validateEntity(Context ctx) {
        return ctx.bodyValidator(Picture.class)
                .check( p -> p.getUrl() != null && !p.getUrl().isEmpty(), "Picture url must be set")
                .check( p -> p.getAlt() != null && !p.getAlt().isEmpty(), "Picture alt must be set")
                .get();
    }
}
