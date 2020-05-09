package com.ichzh.physicalFitness.service.impl;

import com.alibaba.fastjson.JSON;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.service.ResponseService;
import com.ichzh.physicalFitness.util.CommonUtil;
import com.ichzh.physicalFitness.util.JarPathUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service("responseService")
@Slf4j
public class ResponseServiceImpl implements ResponseService {

    /**
     * 响应json
     * @param response
     * @param operaResult
     * @throws IOException
     */
    public void write(HttpServletResponse response, OperaResult operaResult) throws IOException {
        String json = JSON.toJSONString(operaResult);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.close();
    }

    @Override
    public String writeFile(byte[] bytes, String relativePathName, boolean append) {
        int i = relativePathName.lastIndexOf("/");
        String path = null;
        if (i != -1) {
            path = relativePathName.substring(0, i);
            relativePathName = relativePathName.substring(i + 1);
        }
        // 创建目录
        if (StringUtils.isNotEmpty(path)) {
            path = JarPathUtil.mkdirDirectoryOnProjectPath(path);
        }
        String fileName = path + File.separator + relativePathName;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName, append);
            outputStream.write(bytes);
            outputStream.flush();
            return fileName;
        } catch (Exception e) {
            log.error("文件存储失败", e);
        } finally {
            CommonUtil.cloneOutputStream(outputStream);
        }
        return null;
    }
}
