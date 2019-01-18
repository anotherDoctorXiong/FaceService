package faceservice.mapper;

import faceservice.model.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


public interface UserMapper {
    List<User> getAll();

    User getOne(String id);

    void insert(User info);

    void update(User info);

    void delete(String id);

    void deleteGroup(List<String> list);

}
