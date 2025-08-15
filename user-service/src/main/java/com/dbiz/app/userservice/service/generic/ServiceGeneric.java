//package com.dbiz.app.userservice.service.generic;
//
//import com.dbiz.app.userservice.domain.AbstractMappedEntity;
//import com.dbiz.app.userservice.domain.Customer;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.List;
//
//public interface ServiceGeneric<T extends AbstractMappedEntity>  {
//
//
//	Page<T> findAll(Specification specification, Pageable pageable) throws Exception;
//	T save(T entity) throws Exception;
//	void delete(Integer id) throws Exception;
//
//}
