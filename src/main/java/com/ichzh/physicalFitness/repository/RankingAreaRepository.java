package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.RankingArea;
import com.ichzh.physicalFitness.repository.ext.IRankingAreaRepositoryExt;

@Repository
public interface RankingAreaRepository extends BaseRepository<RankingArea, Integer>, IRankingAreaRepositoryExt{

}
