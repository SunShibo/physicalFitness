package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.Ranking;
import com.ichzh.physicalFitness.repository.ext.IRankingRepositoryExt;

@Repository
public interface RankingRepository extends BaseRepository<Ranking, Integer>, IRankingRepositoryExt{

}
