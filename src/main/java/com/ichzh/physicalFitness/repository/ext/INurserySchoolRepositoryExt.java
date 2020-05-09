package com.ichzh.physicalFitness.repository.ext;

import java.util.List;
import java.util.Map;

import com.ichzh.physicalFitness.dto.StudentStatusDto;
import com.ichzh.physicalFitness.model.NurserySchool;

public interface INurserySchoolRepositoryExt {

	public boolean saveSchoolInfo(List<Map<String,Object>> schoolMapList);
	
	/**
	 * 查询上幼儿园入学优选定位附近的园根据经纬度查询附近5公里范围内的幼儿园
	 * @param serviceBlock  入学条件所属服务模块
	 * @param town          条件所属区
	 * @param latitude      纬度
	 * @param longitude     经度
	 * @param maxDistance   公里范围
	 * @param schoolType    学校类型 1：表示公办，2：表示民办，3：表示示范，4：表示民办普惠
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryNurserySchoolMapRange(Integer serviceBlock, Integer town, Double latitude, Double longitude,
			Integer maxDistance, Integer schoolType);
	
	/**
	 * 查询幼儿园列表
	 * @param serviceBlock  入学条件所属服务模块
	 * @param town          条件所属区
	 * @param organizers    举办者
	 * @param isModel       是否示范
	 * @param isInclusive   是否民办普惠
	 * @return
	 */
	public List<NurserySchool> queryNurserySchool(Integer serviceBlock, Integer town, Integer organizers, Integer isModel,
			Integer isInclusive, String memberId);
	
	/**
	 * 查询上幼儿园入学优选分类查询筛选条件页查询举办者
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<StudentStatusDto> queryOrganizers(Integer serviceBlock, Integer town);
	
	/**
	 * 通过学校名称和学校所属区查询幼儿园
	 * @param schoolName 学校名称
	 * @param town 区
	 * @return
	 */
	public List<Object> queryNurserySchoolBy(String schoolName, Integer town);
}
