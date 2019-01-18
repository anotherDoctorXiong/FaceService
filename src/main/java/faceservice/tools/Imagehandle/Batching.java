package faceservice.tools.Imagehandle;

import java.io.File;

public class Batching {


    /**
     * 递归获取某路径下的所有文件，文件夹，并输出
     * @param path
     */

    public static void getFiles(String path) {
        File file = new File(path);
        //获取文件夹下所有图片名称
        File[] files = file.listFiles();

        String[] images = new String[files.length];

        for (int i = 0; i <files.length-1 ; i++) {
            images[i] = files[i].getPath();
        }
    }



}
