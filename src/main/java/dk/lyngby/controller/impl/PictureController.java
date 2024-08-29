package dk.lyngby.controller.impl;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.IController;
import dk.lyngby.dao.impl.PictureDao;
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
    public void read(Context ctx)  {
        // request
        int id = Integer.parseInt(ctx.pathParam("id"));
        // entity
        Picture Picture = dao.read(id);
        // dto
        PictureDto pictureDto = new PictureDto(Picture);
        // response
        ctx.res().setStatus(200);
        ctx.json(pictureDto, PictureDto.class);
    }

    @Override
    public void readAll(Context ctx) {
        // entity
        List<Picture> Pictures = dao.readAll();
        // dto
        List<PictureDto> pictureDtos = PictureDto.toPictureDTOList(Pictures);
        // response
        ctx.res().setStatus(200);
        ctx.json(pictureDtos, PictureDto.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        //Picture jsonRequest = validateEntity(ctx);
        Picture jsonRequest = ctx.bodyAsClass(Picture.class);
        // entity
        Picture Picture = dao.create(jsonRequest);
        // dto
        PictureDto pictureDto = new PictureDto(Picture);
        // response
        ctx.res().setStatus(201);
        ctx.json(pictureDto, PictureDto.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = Integer.parseInt(ctx.pathParam("id"));
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
        int id = Integer.parseInt(ctx.pathParam("id"));
        // entity
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public Picture validateEntity(Context ctx) {
        return ctx.bodyValidator(Picture.class)
                .check( h -> h.getPictureUrl() != null && !h.getPictureUrl().isEmpty(), "Picture address must be set")
                .get();
    }

}
