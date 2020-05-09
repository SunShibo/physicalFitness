package com.ichzh.physicalFitness.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 生成压缩图片
 * @author sunjianbin
 *
 */
public class ImageCompressUtil {
	
	private Image img;  
    private int width;  
    private int height;
    private String renameFile;
    private String extensionFile;
	
	/** 
     * 构造函数 
     */  
    public ImageCompressUtil(String fileName) throws IOException {  
        File file = new File(fileName);// 读入文件  
        img = ImageIO.read(file);      // 构造Image对象  
        width = img.getWidth(null);    // 得到源图宽  
        height = img.getHeight(null);  // 得到源图长  
        extensionFile = getExtension(fileName);
        renameFile = getPath(fileName)+getFileName(fileName)+"_ico."+extensionFile;
    }  
    
    /**
	 * 获取文件名称
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (filePath != null && !filePath.equals("")) {
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
			return fileName.substring(0, fileName.lastIndexOf("."));
		}
		return null;
	}

	/**
	 * 获取文件后缀名
	 * @param filePath
	 * @return
	 */
	public static String getExtension(String filePath) {
		if (filePath != null && !filePath.equals("")) {
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
			return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		}
		return null;
	}
	
	/**
	 * 获取不带文件的路径
	 * @param filePath
	 * @return
	 */
	public static String getPath(String filePath) {
		if (filePath != null && !filePath.equals("")) {
			 filePath = filePath.substring(0,filePath.lastIndexOf(File.separator) + 1);
			return filePath;
		}
		return null;
	}
    
    /** 
     * 按照宽度还是高度进行压缩 
     * @param w int 最大宽度 
     * @param h int 最大高度 
     * @param newFileName 生成的图片名称
     */  
    public void resizeFix(int w, int h,String newFileName) throws IOException {  
        if (width / height > w / h) {  
            resizeByWidth(w,newFileName);  
        } else {  
            resizeByHeight(h,newFileName);  
        }  
    }  
    /** 
     * 以宽度为基准，等比例放缩图片 
     * @param w int 新宽度 
     * @param newFileName 生成的图片名称 不传参数 默认生成 12_ico.jpg 这种格式文件
     */  
    public void resizeByWidth(int w,String newFileName) throws IOException {  
        int h = (int) (height * w / width);  
        resize(w, h,newFileName);  
    }  
    /** 
     * 以高度为基准，等比例缩放图片 
     * @param h int 新高度 
     * @param newFileName 生成的图片名称
     */  
    public void resizeByHeight(int h,String newFileName) throws IOException {  
        int w = (int) (width * h / height);  
        resize(w, h,newFileName);  
    }  
    /** 
     * 强制压缩/放大图片到固定的大小 
     * @param w int 新宽度 
     * @param h int 新高度 
     * @param newFileName 生成的图片名称
     */  
    public void resize(int w, int h,String newFileName) throws IOException {  
        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢  
        BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);   
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图  
        if(null==newFileName){
        	newFileName = renameFile;
        }
        File destFile = new File(newFileName);  
        if (extensionFile.toLowerCase().endsWith("png")) {
        	ImageIO.write(image, extensionFile, destFile);
		}else{
			FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流  
	        //可以正常实现bmp、png、gif转jpg  
//	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
//	        encoder.encode(image); // JPEG编码  
	        out.close();  
		}
    }
    public static void main(String[] args) {
		try {
			ImageCompressUtil iu=new ImageCompressUtil("/Users/heyuan/Desktop/0.png");
			iu.resizeByWidth(320, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
