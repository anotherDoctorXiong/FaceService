package faceservice.mapper;

import faceservice.model.HuaXia;
import faceservice.model.User;

import java.util.List;
import java.util.Map;

public interface HuaXiaMapper {
    //List<User> getUser(String ip);

    HuaXia getOne(String id);

    void insert(HuaXia info);

    void update(HuaXia info);

    void delete(String id);

    void deleteGroup(String ip);

    List<String> queryGroup(String ip);

    List<Map<String,Object>> getGroup(String ip);
}
