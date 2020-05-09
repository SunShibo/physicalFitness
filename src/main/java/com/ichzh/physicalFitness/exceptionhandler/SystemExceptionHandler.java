package com.ichzh.physicalFitness.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ichzh.physicalFitness.domain.OperaResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @RestControllerAdvice 的范围有：
 * ①basePackages：应用在xx包
 * ②basePackageClasses：应用在xx类
 * ③assignableTypes：应用在加了@Controller的类
 * ④annotations：应用在带有xx注解的类或者方法
 */
@RestControllerAdvice
@Slf4j
public class SystemExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public OperaResult defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("发生异常的uri：" + request.getRequestURI());
        log.error("异常详情：", e);
        OperaResult operaResult = new OperaResult();
        operaResult.setMessageTitle("系统发生错误");
        operaResult.setResultDesc("服务器繁忙");
        operaResult.setMessageType(OperaResult.Error);
        return operaResult;
    }
}
