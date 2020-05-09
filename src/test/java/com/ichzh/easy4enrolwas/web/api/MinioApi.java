package com.ichzh.easy4enrolwas.web.api;

import com.ichzh.physicalFitness.service.MinioService;
import com.ichzh.physicalFitness.util.CommonUtil;

import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class MinioApi {

    @Autowired
    private MinioService minioService;

    /**
     * 文件上传到云存储案例
     * @param file
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws InvalidArgumentException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     */
    @RequestMapping("/uploadFileExample")
    public String uploadFileExample(MultipartFile file) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        String objectName = System.currentTimeMillis() + CommonUtil.getExtName(file.getOriginalFilename());
        minioService.uploadFileByDefaultBucket(objectName, file.getInputStream());
        return "上传文件成功：" + objectName;
    }

    /**
     * 文件下载或渲染到页面案例
     * @param request
     * @param response
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws InvalidArgumentException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     */
    @RequestMapping("/downloadFileExample")
    public void downloadFileExample(HttpServletRequest request, HttpServletResponse response) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        minioService.downloadFileByDefaultBucket(request, response, "th.jpg", "乌龟.jpg");
    }

    /**
     * 获取文件访问的 url
     * @param request
     * @return
     */
    @RequestMapping("/getFileUrl")
    public Map<String, String> getFileUrl(HttpServletRequest request) {
        String localFileUrl = MinioService.getLocalFileUrl(request, "th.jpg");
        String remoteFileUrl = MinioService.getRemoteFileUrl(request, "th.jpg");
        Map<String, String> map = new HashMap<>();
        map.put("文件本地访问的url（可用于项目内部调用，如生成pdf加载图片，不需要登录）", localFileUrl);
        map.put("文件远程访问的url(可用于下载、页面渲染,需要登录)", remoteFileUrl);
        return map;
    }
}
