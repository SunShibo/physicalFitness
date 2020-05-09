package com.ichzh.physicalFitness.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.model.SchoolCollection;
import com.ichzh.physicalFitness.repository.SchoolCollectionRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.SchoolCollectionService;

@Service("schoolCollectionService")
public class SchoolCollectionServiceImpl implements SchoolCollectionService {

	@Autowired
	SchoolCollectionRepository schoolCollectionRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;
	
	/**
	 * 收藏或取消收藏一个学校
	 * @param memberId        会员ID.
	 * @param serviceBlock    服务模块.
	 * @param schooId      学校标识号.
	 * @param collectionOrCancel  操作类型    .1: 收藏  0：取消
	 * @return
	 */
	public boolean collectionOrCancel(String memberId, Integer serviceBlock, Integer schooId, Integer collectionOrCancel) {
		
		//收藏
		if (collectionOrCancel.intValue() == 1) {
			//校验是否已经收藏过
			SchoolCollection schoolCollection = schoolCollectionRepository.findByServiceBlockAndMemberIdAndSchoolId(serviceBlock, memberId, schooId);
			if (schoolCollection != null) {
				return true;
			}
			schoolCollection = new SchoolCollection();
			schoolCollection.setMemberId(memberId);
			schoolCollection.setServiceBlock(serviceBlock);
			schoolCollection.setSchoolId(schooId);
			schoolCollection.setCollectionTime(new Date());
			
			schoolCollectionRepository.save(schoolCollection);
			// 写入缓存
			cacheApplicationService.writeCollectionToCache(memberId, schoolCollection);
			
			return true;
			
		}else if(collectionOrCancel.intValue() == 0) { //取消收藏
			schoolCollectionRepository.deleteByServiceBlockAndMemberIdAndSchoolId(serviceBlock, memberId, schooId);
			
			// 从缓存中移除
			cacheApplicationService.removeCollectionFromCache(memberId, serviceBlock, schooId);
			
			return true;
		}
		return false;
	}
	
	/**
	 * 设置学校名称
	 * @param schoolCollections
	 */
	public void setSchoolName(List<SchoolCollection> schoolCollections) {
		
		if (schoolCollections != null && schoolCollections.size() > 0) {
			for (SchoolCollection  oneSchoolCollection : schoolCollections) {
				Integer serviceBlock = oneSchoolCollection.getServiceBlock();
				Integer schoolId = oneSchoolCollection.getSchoolId();
				
				//上幼儿园
				if (Constant.DICT_ID_10001 == serviceBlock.intValue()) {
					
					//从缓存中获取幼儿园信息
					NurserySchool nurserySchool = cacheApplicationService.getNurserySchoolBy(schoolId);
					oneSchoolCollection.setSchoolName(nurserySchool.getSchoolName());
					oneSchoolCollection.setTown(nurserySchool.getTown());
					oneSchoolCollection.setTownName(cacheApplicationService.getDictName(nurserySchool.getTown()));
				}else {
					//从缓存中获取中小学信息
					SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(schoolId);
					oneSchoolCollection.setSchoolName(schoolChoice.getSchoolName());
					oneSchoolCollection.setTown(schoolChoice.getTown());
					oneSchoolCollection.setTownName(cacheApplicationService.getDictName(schoolChoice.getTown()));
				}
			}
		}
	}

	

	
}
