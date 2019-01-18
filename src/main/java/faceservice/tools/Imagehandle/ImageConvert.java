package faceservice.tools.Imagehandle;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片检测及处理
 */

public class ImageConvert {
    /**
     * 图片分辨率判断及压缩
     * @param file
     * @throws IOException
     */
    public static File getVlidImage(File file) throws IOException {


        boolean b = CheckImagesFormatUtil.checkImageElement(file, 1280, 720);
        if (b){
            System.out.println("图片满足注册要求，进入人脸注册阶段");
            return file;
        }else {
            System.out.println("图片格式不满足需求，进入压缩模式。。。。。。");
            File tempimage=new File(ResourceUtils.getURL("classpath:Face/tempface.jpg").getPath());
            FileOutputStream fos = new FileOutputStream(tempimage);
            ImageTools.thumbnail_w_h(file,4096,4096,fos);
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

            System.out.println("图片格式不满足需求，进入压缩模式。。。。。。");

            String f1 = f.getPath();
            System.out.println(f1);
            String[] split = f1.split("\\\\");
            String id = split[split.length-1];
            System.out.println(id);
            File file1 = new File("D:/sucess");

            if (!file1.exists()){
                System.out.println("路径不存在！自动创建D:/sucess 路径 存储压缩过后的图片");
                file1.mkdir();
            }
            File sucess = new File("D:/sucess/"+id);

            FileOutputStream fos = new FileOutputStream(sucess);
            ImageTools.thumbnail_w_h(f,4096,4096,fos);
        }



    }

    public static void main(String[] args) throws IOException {
        File file = new File( "D:\\error");
       imgJudgement1(file);
    }


}
