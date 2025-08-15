package com.dbiz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbiz.app.domain.Favourite;
import com.dbiz.app.domain.id.FavouriteId;

public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {
	
	
	
}
