package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.InfoLatestYear;
import com.ichzh.physicalFitness.repository.ext.IInfoLatestYearRepositoryExt;

@Repository
public interface InfoLatestYearRepository extends BaseRepository<InfoLatestYear, Integer>, IInfoLatestYearRepositoryExt{

}
