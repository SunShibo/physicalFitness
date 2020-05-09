package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SchoolHeatingPower;
import com.ichzh.physicalFitness.repository.ext.ISchoolHeatingPowerRepositoryExt;

@Repository
public interface SchoolHeatingPowerRepository extends BaseRepository<SchoolHeatingPower, Integer>, ISchoolHeatingPowerRepositoryExt{
	
	/**
	 * 根据热力校所属区和学校类型查询热力信息
	 * @param town           区.
	 * @param schoolType     学校类型 1: 小学 2：初中
	 * @return List<SchoolHeatingPower>
	 */
	public List<SchoolHeatingPower> findByTownAndSchoolType(Integer town, Integer schoolType);

}
