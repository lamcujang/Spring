package com.dbiz.app.service;

import java.util.List;

import com.dbiz.app.domain.id.FavouriteId;
import com.dbiz.app.dto.FavouriteDto;

public interface FavouriteService {
	
	List<FavouriteDto> findAll();
	FavouriteDto findById(final FavouriteId favouriteId);
	FavouriteDto save(final FavouriteDto favouriteDto);
	FavouriteDto update(final FavouriteDto favouriteDto);
	void deleteById(final FavouriteId favouriteId);
	
}
