package com.ichzh.physicalFitness.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.InfoLatestYear;
import com.ichzh.physicalFitness.repository.InfoLatestYearRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/infoLatestYear"})
public class InfoLatestYearApi {

	@Autowired
	private InfoLatestYearRepository infoLatestYearRepository;
	
	/**
	 * 查询入学条件
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @param studentStatus   标签_学籍
	 * @param householdRegistration  标签_户籍
	 * @param residence 标签_居住
	 * @return
	 */
	@RequestMapping(value="/queryInfoLatestYear", method= {RequestMethod.POST})
	public OperaResult queryAdmConBy(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<InfoLatestYear> infoLatestes = infoLatestYearRepository.queryBy(serviceBlock, town);
			
			result.setResultCode(OperaResult.Success);
			result.setData(infoLatestes);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
}
