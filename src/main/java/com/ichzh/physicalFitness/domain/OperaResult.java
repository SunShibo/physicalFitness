package com.ichzh.physicalFitness.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 操作结果
 * 
 * @author smileYange
 *
 */
@Data
public class OperaResult implements Serializable {
	
	public static final String Success = "success";
	public static final String Info = "info";
	public static final String Wait = "wait";
	public static final String Warning = "warning";
	public static final String Error = "error";
    //登录超时
	public static final String RESULT_CODE_LOGIN_TIMEOUT = "000";
	// 结果码 000 登录已超时
	private String resultCode;
	
	// 结果描述(消息框的内容)
	private String resultDesc;
	
	// 消息类型(可以取的值如下：success,info,wait,warning,error)
	private String messageType;

	// 消息框的标题
	private String messageTitle;
	
	// 有意义的业务数据
	private Object data;

	public OperaResult() {

	}

	public OperaResult(String messageTitle, String resultDesc, String messageType) {
		this.messageTitle = messageTitle;
		this.resultDesc = resultDesc;
		this.messageType = messageType;
	}
	
}
