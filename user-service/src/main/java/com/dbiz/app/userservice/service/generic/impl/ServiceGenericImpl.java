//package com.dbiz.app.userservice.service.generic.impl;
//
//
//import com.dbiz.app.userservice.domain.AbstractMappedEntity;
//import com.dbiz.app.userservice.repository.generic.GenericRepository;
//import com.dbiz.app.userservice.service.generic.ServiceGeneric;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//public class ServiceGenericImpl<T extends AbstractMappedEntity> implements ServiceGeneric<T> {
//
//	protected GenericRepository<T> genericRepository;
//
//	@Override
//	public Page<T> findAll(Specification specification, Pageable pageable) throws Exception {
//		try {
//			return genericRepository.findAll(specification, pageable);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	@Override
//	public T save(T entity) throws Exception {
//		try {
//			return genericRepository.save(entity);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	@Override
//	public void delete(Integer id) throws Exception {
//		try {
//			genericRepository.deleteById(id);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//}
