package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.AddressInfo;
import com.ichzh.physicalFitness.repository.ext.IAddressInfoRepositoryExt;

@Repository
public interface AddressInfoRepository extends BaseRepository<AddressInfo, Integer>, IAddressInfoRepositoryExt{

	public List<AddressInfo> findByMemberIdAndAddressTypeAndIfDefault(String memberId, Integer addressType, Integer ifDefault);
}
