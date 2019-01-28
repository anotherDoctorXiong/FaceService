package faceservice.mapper;

import faceservice.model.FacePass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface FacePassMapper {
    FacePass getOne(String id);

    String getFace_token(String id);

    String getId(String face_token);

    void deleteGroup(String group);

    void insert(FacePass info);

    void update(FacePass info);

    void delete(@Param("id") String id,@Param("face_token") String face_token);

    List<Map<String,Object>> getGroup(String group);

    List<String> getFace_TokenList(String group);

    List<String> getIdList(String group);
}
