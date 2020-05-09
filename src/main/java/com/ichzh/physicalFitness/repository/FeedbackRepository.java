package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.Feedback;
import com.ichzh.physicalFitness.repository.ext.IFeedbackRepositoryExt;

@Repository
public interface FeedbackRepository extends BaseRepository<Feedback, Integer>, IFeedbackRepositoryExt{

}
