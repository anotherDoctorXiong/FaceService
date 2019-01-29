package faceservice.tools.Constraint;

import org.springframework.stereotype.Component;
import java.io.File;
@Component
public class Image {
    public boolean isPic(File file) {
        String filename=file.getName();
        String type=filename.split("\\.")[1];
        if(type.equals("jpg")||type.equals("jpeg")||type.equals("png")){
            return true;
        }else
            return false;
    }
}
