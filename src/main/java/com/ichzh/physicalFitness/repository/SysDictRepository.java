package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SysDict;

import java.util.List;

/**
 * 字典dao
 */
@Repository
public interface SysDictRepository extends BaseRepository<SysDict, Integer> {

    List<SysDict> findByDictType(Integer dictType);
}
