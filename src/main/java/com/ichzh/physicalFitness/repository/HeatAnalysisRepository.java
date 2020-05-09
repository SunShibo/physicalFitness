package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.HeatAnalysis;
import com.ichzh.physicalFitness.repository.ext.IHeatAnalysisRepositoryExt;

@Repository
public interface HeatAnalysisRepository extends BaseRepository<HeatAnalysis, Integer>, IHeatAnalysisRepositoryExt{

}
