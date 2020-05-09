package com.ichzh.physicalFitness.domain;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MyPageImpl<T> extends PageImpl<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 描述
	 */
	private String describe;
	/**
	 * 相应码
	 */
	private int code;

	public MyPageImpl(List<T> content) {
		super(content);
		// TODO Auto-generated constructor stub
	}

	public MyPageImpl(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
		// TODO Auto-generated constructor stub
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	

}
