package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.dto.AidedAllocateRangePrimSchoolDto;
import com.ichzh.physicalFitness.dto.SchoolDto;
import com.ichzh.physicalFitness.dto.StreetCommunityDto;
import com.ichzh.physicalFitness.model.AidedAllocateRange;

public interface IAidedAllocateRangeRepositoryExt {
	
	/**
	 * 查询派位范围
	 * @param serviceBlock         派位范围所属服务模块
	 * @param town                 派位范围所属区
	 * @param schoolDistrictName   学区或片区名称
	 * @param schoolCode           小学\中学学校代码
	 * @param schoolName           小学\中学名称代码
	 * @return
	 */
	List<AidedAllocateRange> queryBy(Integer serviceBlock, Integer town, String schoolDistrictName, String schoolCode,
			String schoolName);
	
	/**
	 * 模糊搜索学区或片区名称
	 * @param serviceBlock        派位范围所属服务模块
	 * @param town                派位范围所属区
	 * @param schoolDistrictName  学区或片区名称
	 * @return
	 */
	List<StreetCommunityDto> querySchoolDistrict(Integer serviceBlock, Integer town, String schoolDistrictName);
	
	/**
	 * 模糊搜索小学或中学名称
	 * @param serviceBlock         派位范围所属服务模块
	 * @param town                 派位范围所属区
	 * @param schoolDistrictName   学区或片区名称
	 * @param schoolCode           小学\中学学校代码
	 * @param schoolName           小学\中学名称代码
	 * @return
	 */
	List<SchoolDto> querySchoolName(Integer serviceBlock, Integer town, String schoolDistrictName,String schoolCode,
			String schoolName);
	
	
	/**
	 * 批量修改表小学及中学code值
	 * @return
	 */
	public String batchUpdateTable();
	
	/**
	 * 查询派位范围中的小学
	 * @param serviceBlock  派位服务模块.
	 * @param town          派位所属区.
	 * @param schoolName    小学名称关键字 这里采用like查询
	 * @return List<AidedAllocateRangePrimSchoolDto>.
	 */
	List<AidedAllocateRangePrimSchoolDto> queryAidedAllocateRangePrimSchoolBy(Integer serviceBlock, Integer town, String schoolName);
	/**
	 * 查询派位范围中的中学
	 * @param serviceBlock  派位服务模块.
	 * @param town          派位所属区.
	 * @param priSchCode    小学学校代码
	 * @return List<AidedAllocateRangePrimSchoolDto>.
	 */
	List<AidedAllocateRangePrimSchoolDto> queryAidedAllocateRangeMiddSchoolBy(Integer serviceBlock, Integer town, String priSchCode);
	
	
	
	

}
