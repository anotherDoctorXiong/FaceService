package faceservice.mapper;

import faceservice.model.keyModel;



import java.util.List;

public interface keyMapper {
    List<keyModel> getAll();

    keyModel getOne(String id);

    List<String> getGroup(String group);

    void deleteGroup(String group);

    void insert(keyModel info);

    void update(keyModel info);

    void delete(String id);

}
