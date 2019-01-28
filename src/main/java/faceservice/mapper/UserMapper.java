package faceservice.mapper;

import faceservice.model.User;
import java.util.List;


public interface UserMapper {
    List<User> getAll();

    User getOne(String id);

    void insert(User info);

    void update(User info);

    void delete(String id);

    void updateFace(String id,byte[] face);

    void deleteGroup(List<String> list);

}
