package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.MinioConfig;
import com.ichzh.physicalFitness.service.MinioService;
import com.ichzh.physicalFitness.util.CommonUtil;

import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioConfig minioConfig;

    private MinioClient minioClient;
    
    @Autowired
    public MinioServiceImpl(MinioConfig minioConfig) {
    	this.minioConfig = minioConfig;
    	try {
            this.minioClient = new MinioClient(minioConfig.getEndpoint(), minioConfig.getAccessKey(), minioConfig.getSecretKey());
        } catch (InvalidEndpointException|InvalidPortException e) {
            log.error("云存储客户端启动失败", e);
        }
    }

    @Override
    public MinioClient getMinioClient() throws InvalidPortException, InvalidEndpointException {
        if (minioClient == null) {
            minioClient = new MinioClient(minioConfig.getEndpoint(), minioConfig.getAccessKey(), minioConfig.getSecretKey());
        }
        return minioClient;
    }

    @Override
    public String uploadFileByDefaultBucket(String objectName, InputStream inputStream) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        String contentType = this.getFileContentType(objectName);
        minioClient.putObject(minioConfig.getBucketName(), objectName, inputStream, contentType);
        return contentType;
    }

    @Override
    public String uploadLocalFileByDefaultBucket(String objectName, String path) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        // 读取本地文件
        InputStream inputStream = new FileInputStream(new File(path));
        // 上传到云存储
        return uploadFileByDefaultBucket(objectName, inputStream);
    }

    @Override
    public InputStream getFileByDefaultBucket(String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        InputStream inputStream = minioClient.getObject(minioConfig.getBucketName(), objectName);
        return inputStream;
    }

    @Override
    public byte[] getFileBytesByDefaultBucket(String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        InputStream inputStream = minioClient.getObject(minioConfig.getBucketName(), objectName);
        return CommonUtil.getByteByInputStream(inputStream);
    }

    @Override
    public void downloadFileByDefaultBucket(HttpServletRequest request, HttpServletResponse response, String objectName, String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        InputStream inputStream = minioClient.getObject(minioConfig.getBucketName(), objectName);
        // 下载文件名乱码处理和响应处理
        String userAgent = request.getHeader("USER-AGENT");
        if (userAgent.contains("Firefox")) {
            fileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        String contentType = this.getFileContentType(fileName);
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        // 转为输出流
        long length = StreamUtils.copy(inputStream, outputStream);
        response.setContentLength((int) length);
        // 输出到浏览器
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @Override
    public void viewFileByByDefaultBucket(HttpServletRequest request, HttpServletResponse response, String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        InputStream inputStream = minioClient.getObject(minioConfig.getBucketName(), objectName);
        // 下载文件名乱码处理和响应处理
        response.setCharacterEncoding("UTF-8");
        String contentType = this.getFileContentType(objectName);
        response.setContentType(contentType);
        ServletOutputStream outputStream = response.getOutputStream();
        StreamUtils.copy(inputStream, outputStream);
        outputStream.close();
        inputStream.close();
    }

    @Override
    public String getFileContentType(String fileName) {
        if (fileName == null) {
            return null;
        }
        int i = fileName.lastIndexOf(".");
        if (i == -1) {
            return "application/octet-stream";
        }
        if (i == fileName.length() - 1) {
            return "application/x-";
        }
        String ext = fileName.substring(i + 1);
        String ct = minioConfig.getContentTypeMap().get(ext);
        if (ct == null) {
            return "application/octet-stream";
        }
        return ct;
    }

    @Override
    public String getImageBase64StrByDefaultBucket(String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        InputStream inputStream = minioClient.getObject(minioConfig.getBucketName(), objectName);
        Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(StreamUtils.copyToByteArray(inputStream));
        inputStream.close();
        String contentType = getFileContentType(objectName);
        return "data:" + contentType + ";base64," + encode;
    }

    @Override
    public void removeObjectByDefaultBucket(String objectName) {
    	// 删除不做抛出异常，因为一般删除失败不受理
    	try {
    		minioClient.removeObject(minioConfig.getBucketName(), objectName);
    	} catch (Exception e) {
    		log.warn("云文件删除失败", e);
    	}
    }

    @Override
    public String getDownloadFileUrl(String objectName, Integer expiry) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return minioClient.presignedGetObject(minioConfig.getBucketName(), objectName, expiry);
    }

}
