package faceservice;


import faceservice.mapper.UserMapper;
import faceservice.model.User;
import faceservice.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.List;


public class SyncData implements CommandLineRunner {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private FaceService faceService;

    @Override
    public void run(String... args) throws Exception {
        List<User> list=mapper.getAll();
        if(list!=null){
            System.out.println("同步数据中.......");
            list.forEach(p->{
                /*try {
                    if(faceService.queryFace(p.getId()).getStatusCode().value()!=200){
                        mapper.delete(p.getId());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            });
            System.out.println("同步完成");
        }
    }
}
