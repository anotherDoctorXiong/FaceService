package faceservice;


import faceservice.mapper.keyMapper;
import faceservice.model.keyModel;
import faceservice.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyncData implements CommandLineRunner {
    @Autowired
    private keyMapper mapper;
    @Autowired
    private FaceService faceService;

    @Override
    public void run(String... args) throws Exception {
        List<keyModel> list=mapper.getAll();
        if(list!=null){
            System.out.println("同步数据中.......");
            list.forEach(p->{
                try {
                    if(faceService.queryFace(p.getId()).getStatusCode().value()!=200){
                        mapper.delete(p.getId());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("同步完成");
        }
    }
}