package com.ichzh.physicalFitness.web.api;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Feedback;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.FeedbackRepository;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/feedback"})
public class FeedBackApi {

	@Autowired
	UserService userService;
	@Autowired
	FeedbackRepository feedbackRepository;
	
	/**
	 * 检查当前登录用户是否为付费会员
	 * @return
	 */
	@RequestMapping(value="/writeFeedback", method= {RequestMethod.POST})
	public OperaResult writeFeedback(@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//获得当前登录用户
		Member currentLoginUser = userService.queryNewestMember(request);
		if (currentLoginUser == null) {
			result.setResultCode(OperaResult.Error);
			//2: 当前用户的登录状态已失效
			resultData.put("write_result", "2");
			result.setData(resultData);
			return result;
		}else {
			if (StringUtils.isEmpty(content)) {
				//3:反馈内容为空
				result.setResultCode(OperaResult.Error);
				resultData.put("write_result", "3");
				result.setData(resultData);
				return result;
			}
			if (content.trim().length() > 250) {
				result.setResultCode(OperaResult.Error);
				// 4: 内容超过250个字符
				resultData.put("write_result", "4");
				result.setData(resultData);
				return result;
			}
			
			Feedback feedBack = new Feedback();
			feedBack.setMemberId(currentLoginUser.getMemberId());
			feedBack.setContent(content);
			
			feedbackRepository.save(feedBack);
			
			result.setResultCode(OperaResult.Success);
			//1：写入成功
			resultData.put("write_result", "1");
			result.setData(resultData);
			
		}
		
		return result;
	}
}
