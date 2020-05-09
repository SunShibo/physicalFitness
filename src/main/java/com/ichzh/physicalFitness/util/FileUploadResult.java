package com.ichzh.physicalFitness.util;
/**
 * @author smileyang
 */
public class FileUploadResult {

	/**
	 * 文件上传后反馈的相对地址
	 * 当该参数为空时，有可能是上传文件失败，具体原因可以看参数uploadFeedback的值.
	 */
	private String fileUri;
	/**
	 * 正常情况下该属性的值为空，上传失败后反馈失败原因.
	 */
	private String uploadFeedback;
	public String getFileUri() {
		return fileUri;
	}
	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}
	public String getUploadFeedback() {
		return uploadFeedback;
	}
	public void setUploadFeedback(String uploadFeedback) {
		this.uploadFeedback = uploadFeedback;
	}
	
	
}

