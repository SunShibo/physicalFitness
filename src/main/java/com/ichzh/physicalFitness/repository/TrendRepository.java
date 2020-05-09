package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.Trend;
import com.ichzh.physicalFitness.repository.ext.ITrendRepositoryExt;

@Repository
public interface TrendRepository extends BaseRepository<Trend, Integer>, ITrendRepositoryExt{
	
	public List<Trend> findByServiceBlockAndSchoolCode(Integer serviceBlock, String schoolCode);

}
