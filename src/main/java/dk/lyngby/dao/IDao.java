package dk.lyngby.dao;

import dk.lyngby.model.User;

import java.util.List;

public interface IDao<T, D> {

    T read(D d);
    List<T> readAll();
    T create(T t);
    T update(D d, T t);

    User update(User user);

    void delete(D d);
    boolean validatePrimaryKey(D d);

}
