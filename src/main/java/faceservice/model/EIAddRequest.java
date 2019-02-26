package faceservice.model;

import faceservice.tools.Constraint.ValidHost;
import faceservice.tools.Constraint.ValidImage;
import faceservice.tools.Imagehandle.ImageConvert;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.*;

import static faceservice.tools.Imagehandle.ImageTools.isPic;

public class EIAddRequest {
    @Pattern(regexp = "\\d{10}",message = "device_id is a number of ten digit")
    private String id;

    @Pattern(regexp ="[\\u4e00-\\u9fa5]+",message = "name is only allowed chinese")
    @NotEmpty(message = "name is not allowed to be empty or null")
    private String name;
    @ValidHost(message = "invalid ip")
    private String ip;
    @NotNull(message = "image is not allowed to be null")
    @ValidImage(message = "image is only allowed jpg/jpeg/png")
    private File image;
    @Pattern(regexp = "\\d{8}",message = "device_id is a number of eight digit")
    private String device;




    private String days;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

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
                }else {
                    image.delete();
                    this.image = image;
                }
            } catch (IOException e) {
                e.toString();
            }
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public byte[] getBytes(){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(this.image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
