package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.dto.RangeDto;
import com.ichzh.physicalFitness.dto.SchoolDto;
import com.ichzh.physicalFitness.dto.StreetCommunityDto;
import com.ichzh.physicalFitness.model.JzdSchool;

public interface IJzdSchoolRepositoryExt {

	/**
	 * 查询居住地对应街道的个数
	 * @param serviceBlock   居住地所属服务模块
	 * @param town           居住地所属区
	 * @return
	 */
	public int queryStreetNum(Integer serviceBlock, Integer town);
	
	/**
	 * 查询居住地对应社区的个数
	 * @param serviceBlock   居住地所属服务模块
	 * @param town           居住地所属区
	 * @return
	 */
	public int queryCommunityNum(Integer serviceBlock, Integer town);
	
	/**
	 * 查询居住地对应地址的个数
	 * @param serviceBlock   居住地所属服务模块
	 * @param town           居住地所属区
	 * @return
	 */
	public int queryDetailAddressNum(Integer serviceBlock, Integer town);	
	
	/**
	 * 查询居住地对应学校
	 * @param serviceBlock
	 * @param town
	 * @param streetName
	 * @param communityName
	 * @param detailAddress
	 * @return
	 */
	public List<JzdSchool> queryBy(Integer serviceBlock, Integer town, String streetName, String communityName,
			String detailAddress);
	
	/**
	 * 模糊搜索街道信息
	 * @param serviceBlock  居住地所属服务模块
	 * @param town          居住地所属区  
	 * @param streetName    搜索的街道关键字 (长度至少了三)
	 * @return
	 */
	public List<StreetCommunityDto> queryStreetName(Integer serviceBlock, Integer town, String streetName);
	
	/**
	 * 模糊搜索社区信息
	 * @param serviceBlock  居住地所属服务模块
	 * @param town          居住地所属区  
	 * @param streetName    街道全名(空或全名)
	 * @param CommunityName    搜索的社区关键字 (长度至少了三)
	 * @return
	 */
	public List<StreetCommunityDto> queryCommunityName(Integer serviceBlock, Integer town,  String streetName, String communityName);
	
	/**
	 * 模糊搜索地址信息
	 * @param serviceBlock  居住地所属服务模块
	 * @param town          居住地所属区  
	 * @param streetName    街道全名(空或全名)
	 * @param CommunityName    社区全名(空或全名)
	 * @param detailAddress    搜索的地址关键字 (长度至少了三)
	 * @return 
	 */
	public List<StreetCommunityDto> queryDetailAddress(Integer serviceBlock, Integer town, String streetName, String communityName, String detailAddress);
	
	/**
	 * 模糊搜索查询学校名称
	 * @param serviceBlock  居住地所属服务模块
	 * @param town          居住地所属区  
	 * @param schoolName    查询学校名称
	 * @return
	 */
	public List<SchoolDto> queryJzdSchoolInfo(Integer serviceBlock, Integer town, String schoolName);
	
	/**
	 * 查询街道社区地址列表
	 * @param serviceBlock  居住地所属服务模块
	 * @param town          居住地所属区  
	 * @param schoolName    查询学校名称
	 * @return
	 */
	public List<RangeDto> queryStreetCommunityAddress(Integer serviceBlock, Integer town, String schoolName);
}
