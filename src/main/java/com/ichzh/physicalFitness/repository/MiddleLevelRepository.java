package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.MiddleLevel;
import com.ichzh.physicalFitness.repository.ext.IMiddleLevelRepositoryExt;

@Repository
public interface MiddleLevelRepository extends BaseRepository<MiddleLevel, Integer>, IMiddleLevelRepositoryExt{

}
