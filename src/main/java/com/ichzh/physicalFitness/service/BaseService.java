/**
 * 
 */
package com.ichzh.physicalFitness.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Sort;

/**
 * @author audin
 *
 */
public interface BaseService<T, ID extends Serializable> {
	T save(T obj);
	<S extends T> List<S> save(Iterable<S> entities);
	List<T> findAll();
	List<T> findAll(Sort sort);
	List<T> findAll(Iterable<ID> ids);
	void delete(ID id);
	void delete(T obj);
}
