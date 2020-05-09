package com.ichzh.physicalFitness.service;

import javax.servlet.http.HttpServletResponse;

import com.ichzh.physicalFitness.domain.OperaResult;

import java.io.IOException;

public interface ResponseService {

    /**
     * 操作结果，响应json
     * @param response
     * @param operaResult
     * @throws IOException
     */
    void write(HttpServletResponse response, OperaResult operaResult) throws IOException;

    /**
     * 将数据写入到硬盘
     * 会在项目目录下自动创建目录
     * 目录文件的统一使用“/”分隔，不使用File.separator
     * @param bytes
     * @param relativePathName 相对项目路径名称 如：temp/data.txt
     * @return
     */
    String writeFile(byte[] bytes, String relativePathName, boolean append);
}
