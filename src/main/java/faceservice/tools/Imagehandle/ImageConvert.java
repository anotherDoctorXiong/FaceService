package faceservice.tools.Imagehandle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片检测及处理
 */
@Slf4j
public class ImageConvert {
    /**
     * 图片分辨率判断及压缩
     * @param file
     * @throws IOException
     */
    public static File getVlidImage(File file) throws IOException {


        boolean b = CheckImagesFormatUtil.checkImageElement(file, 1680, 1200);
        if (b){
            return file;
        }else {
            log.info("压缩传入的图片");
            File tempimage=new File(ResourceUtils.getURL("classpath:Face/tempface.jpg").getPath());
            FileOutputStream fos = new FileOutputStream(tempimage);
            ImageTools.thumbnail_w_h(file,1280,720,fos);
            return tempimage;
        }
    }

    /**
     * 图片格式转换批处理
     * @param file
     * @throws IOException
     */
    public static void imgJudgement1(File file) throws IOException {

        //获取文件夹下所有图片名称
        File[] files = file.listFiles();

        for (File f: files
             ) {



            String f1 = f.getPath();
            System.out.println(f1);
            String[] split = f1.split("\\\\");
            String id = split[split.length-1];
            System.out.println(id);
            File file1 = new File("D:/sucess");

            if (!file1.exists()){
                file1.mkdir();
            }
            File sucess = new File("D:/sucess/"+id);

            FileOutputStream fos = new FileOutputStream(sucess);
            ImageTools.thumbnail_w_h(f,4096,4096,fos);
        }



    }
}
