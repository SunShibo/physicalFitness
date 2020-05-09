package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.ext.IUnifiedAccountRepositoryExt;

@Repository
public interface UnifiedAccountRepository extends BaseRepository<UnifiedAccount, Integer> , IUnifiedAccountRepositoryExt{

    UnifiedAccount findByAppIdAndOpenId(String appId, String openId);

    int deleteByUnifiedAccountId(Integer unifiedAccountId);
    
    UnifiedAccount findByOpenIdAndType(String openId, Integer type);
    
    UnifiedAccount findByUnionIdAndType(String unionId, Integer type);
}
