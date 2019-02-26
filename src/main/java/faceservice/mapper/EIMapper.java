package faceservice.mapper;

import faceservice.model.EI;
import faceservice.model.HuaXia;

public interface EIMapper {
    void insert(EI info);

    void update(HuaXia info);

    void delete(String id);
    EI getOne(String id);
}
