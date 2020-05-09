package com.ichzh.physicalFitness.service;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;

/**
 * Minio文件系统云服务
 */
public interface MinioService {

    /**
     * 获取云文件访问的url（可以远程访问，需要登录）
     * 主要是页面下载图片或渲染图片使用
     * @param request
     * @param objectName
     * @return
     */
    static String getRemoteFileUrl(HttpServletRequest request, String objectName) {
        return request.getRequestURL().toString().replace(request.getServletPath(), "") + "/api/common/download_file?objectName=" + objectName;
    }

    /**
     * 获取云文件访问的url(不能远程访问，不需要登录)
     * 主要是生成 pdf 时，加载云文件用到
     * @param request
     * @param objectName
     * @return
     */
    static String getLocalFileUrl(HttpServletRequest request, String objectName) {
        return request.getScheme() + "://127.0.0.1:" + request.getLocalPort() + request.getContextPath() + "/images/common/download_file?objectName=" + objectName;
    }

    /**
     * 获取文件云服务客户端
     * @return
     */
    MinioClient getMinioClient() throws InvalidPortException, InvalidEndpointException;

    /**
     * 上传文件到云服务（bucketName默认配置在配置文件中，会自动获取）
     * @param objectName 云服务文件存储名称，尽量带后缀名，用来判断所属ContentType类型
     * @param inputStream 文件流
     * @return 返回文件的ContentType类型
     */
    String uploadFileByDefaultBucket(String objectName, InputStream inputStream) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidPortException, InvalidEndpointException;

    /**
     * 上传文件到云服务（bucketName默认配置在配置文件中，会自动获取）
     * @param objectName 云服务文件存储名称，尽量带后缀名，用来判断所属ContentType类型
     * @param path 文件绝对路径
     * @return 返回文件的ContentType类型
     */
    String uploadLocalFileByDefaultBucket(String objectName, String path) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException;

    /**
     * 从云服务获取文件流
     * @param objectName 云服务文件存储名称
     * @return
     */
    InputStream getFileByDefaultBucket(String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidPortException, InvalidEndpointException;

    /**
     * 从云服务获取二进制文件
     * @param objectName 云服务文件存储名称
     * @return
     */
    byte[] getFileBytesByDefaultBucket(String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 从云服务获取文件，并输出到浏览器
     * @param response HttpServletResponse 响应对象
     * @param objectName 文件云服务存储名称
     * @param fileName 文件下载到浏览器的名称
     */
    void downloadFileByDefaultBucket(HttpServletRequest request, HttpServletResponse response, String objectName, String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidPortException, InvalidEndpointException;

    /**
     * 从云服务获取文件，并在浏览器预览（主要图片预览使用）
     * @param request
     * @param response
     * @param objectName
     */
    void viewFileByByDefaultBucket(HttpServletRequest request, HttpServletResponse response, String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 获取上传文件的ContentType类型
     * @param fileName 文件名称，包含前缀和后缀
     * @return
     */
    String getFileContentType(String fileName);

    /**
     * 从云服务获取图片，并转化为base64字符串
     * @param objectName 文件云服务存储名称
     * @return
     */
    String getImageBase64StrByDefaultBucket(String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidPortException, InvalidEndpointException, InterruptedException;

    /**
     * 删除云存储指定文件
     * @param objectName
     */
    void removeObjectByDefaultBucket(String objectName);

    /**
     * 生成一个给HTTP GET请求用的presigned URL。浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     * @param objectName 存储桶里的对象名称。
     * @param expiry 失效时间（以秒为单位），默认是7天，不得大于七天。
     * @return
     */
    String getDownloadFileUrl(String objectName, Integer expiry) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;
}
