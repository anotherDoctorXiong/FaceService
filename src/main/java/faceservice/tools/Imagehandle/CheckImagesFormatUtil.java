package faceservice.tools.Imagehandle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片检测
 */
public class CheckImagesFormatUtil {

    /**
     * 图片的像素判断
     * @param file 文件
     * @param imageWidth 图片宽度
     * @param imageHeight 图片高度
     * @return true：上传图片宽度和高度都小于等于规定最大值
     * @throws IOException
     */
    public static boolean checkImageElement(File file, int imageWidth, int imageHeight) throws IOException {
        Boolean result = false;
        if (!file.exists()) {
            return false;
        }
        BufferedImage bufferedImage = ImageIO.read(file);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (bufferedImage != null && height == imageHeight && width == imageWidth) {
            result = true;
        }
        return result;
    }

    /**
     * 校验图片比例
     * @param file 图片
     * @param imageWidth 宽
     * @param imageHeight 高
     * @return true：符合要求
     * @throws IOException
     */
    public static boolean checkImageScale(File file, int imageWidth, int imageHeight) throws IOException {
        Boolean result = false;
        if (!file.exists()) {
            return false;
        }
        BufferedImage bufferedImage = ImageIO.read(file);
        int width = bufferedImage.getWidth();
        System.out.println(width);
        int height = bufferedImage.getHeight();
        System.out.println(height);

        if (imageHeight != 0 && height != 0) {

            int scale1 = imageHeight / imageWidth;
            int scale2 = height / width;
            if (scale1 == scale2) {
                result = true;
            }
        }

        return result;
    }

}