package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.AidedAllocateRange;
import com.ichzh.physicalFitness.repository.ext.IAidedAllocateRangeRepositoryExt;

@Repository
public interface AidedAllocateRangeRepository extends BaseRepository<AidedAllocateRange, Integer>,IAidedAllocateRangeRepositoryExt {

}
