package com.ichzh.physicalFitness.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JarPathUtil {

    /**
     * 获取项目所在路径(包括jar)
     *
     * @return
     */
    public static String getProjectPath() {
        java.net.URL url = JarPathUtil.class.getProtectionDomain().getCodeSource()
                .getLocation();
        String filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
            log.info("filePath1:" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.contains(".jar")) {
            //由于版本由1.3.3更新为1.5.22打成包路径从根路径变为/BOOT-INF/classes/需要从新修改取根路径问题
            //首先截取.jar之前的路径
            filePath = filePath.substring(0, filePath.indexOf(".jar"));
            filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
            log.info("filePath2:" + filePath);
        }
//        log.info("filePath3:"+filePath.substring(filePath.length()-6));
//        if(filePath.substring(filePath.length()-6).contains(".jar")) {
//        	filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
//        	log.info("filePath2:"+filePath);
//        }
        java.io.File file = new java.io.File(filePath);
        filePath = file.getAbsolutePath();
        log.info("项目所在的绝对路径--->" + filePath);
        return filePath;
    }

    /**
     * 项目所在根目录，即jar包所在目录
     *
     * @return
     */
    public static String getRootPath() throws UnsupportedEncodingException {
        // 项目的编译文件的根目录
        String path = URLDecoder.decode(PathUtil.class.getResource("/").getPath(), String.valueOf(StandardCharsets.UTF_8));
        if (path.startsWith("file:")) {
            int i = path.indexOf(".jar!");
            path = path.substring(0, i);
            path = path.replaceFirst("file:", "");
        }
        // 项目所在的目录
        return new File(path).getParentFile().getAbsolutePath();
    }

    /**
     * 在项目根目录下创建目录
     *
     * @param directoryName 首字符不能是“/”
     * @return
     */
    public static String mkdirDirectoryOnBasePath(String directoryName) throws UnsupportedEncodingException {
        // 项目的编译文件的根目录
        String path = URLDecoder.decode(PathUtil.class.getResource("/").getPath(), String.valueOf(StandardCharsets.UTF_8));
        if (path.startsWith("file:")) {
            int i = path.indexOf(".jar!");
            path = path.substring(0, i);
            path = path.replaceFirst("file:", "");
            path = new File(path).getParent();
        } else {
            path = new File(path).getParentFile().getParent();
        }
        path = path + File.separator + directoryName;
        File file = new File(path);
        if (!file.isDirectory()) {
            boolean mkdirs = file.mkdirs();
            log.info("在项目根目录下，创建子目录 " + directoryName + " 结果：" + mkdirs);
        }
        return file.getAbsolutePath();
    }

    /**
     * 项目的根目录下创建目录
     * 未打成jar包时，目录与src目录同级
     * 打成jar包后，与jar包同级
     *
     * @param directoryName 首字符不能是“/”
     * @return
     */
    public static String mkdirDirectoryOnProjectPath(String directoryName) {
        File file = new File(directoryName);
        if (!file.isDirectory()) {
            boolean mkdirs = file.mkdirs();
            log.info("在项目根目录下，创建子目录 " + directoryName + " 结果：" + mkdirs);
        }
        return file.getAbsolutePath();
    }
}
