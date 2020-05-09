package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.HighLevel;
import com.ichzh.physicalFitness.repository.ext.IHighLevelRepositoryExt;

@Repository
public interface HighLevelRepository extends BaseRepository<HighLevel, Integer>, IHighLevelRepositoryExt{

}
