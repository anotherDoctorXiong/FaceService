package faceservice.model;


import faceservice.tools.Constraint.ValidImage;
import faceservice.tools.Imagehandle.ImageConvert;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static faceservice.tools.Imagehandle.ImageTools.isPic;

public class FacePassAddRequest implements Serializable{

    @NotEmpty(message = "id is not allowed to be empty or null")
    private String id;

    @Pattern(regexp ="[\\u4e00-\\u9fa5]+",message = "name is only allowed chinese")
    @NotEmpty(message = "name is not allowed to be empty or null")
    private String name;
    @NotNull
    @Pattern(regexp = "^groupname__[0-9]{4}",message = "invalid groupname")
    private String group;
    @NotNull(message = "image is not allowed to be null")
    @ValidImage(message = "image is only allowed jpg/jpeg/png")
    private File image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }



    public File getImage() {
        return image;
    }

    public void setImage(MultipartFile file) {
        if(file!=null){
            File file1 = new File("D:/FaceImage");
            if (!file1.exists()){
                file1.mkdir();
            }
            File image = new File("D:/FaceImage/"+file.getOriginalFilename());
            try {
                file.transferTo(image);
                if(isPic(image)){
                    this.image = ImageConvert.getVlidImage(image);
                }else{
                    this.image=image;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
