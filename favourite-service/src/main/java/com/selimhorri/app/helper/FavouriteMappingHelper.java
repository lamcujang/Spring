package com.dbiz.app.helper;

import com.dbiz.app.domain.Favourite;
import com.dbiz.app.dto.FavouriteDto;
import com.dbiz.app.dto.ProductDto;
import com.dbiz.app.dto.UserDto;

public interface FavouriteMappingHelper {
	
	public static FavouriteDto map(final Favourite favourite) {
		return FavouriteDto.builder()
				.userId(favourite.getUserId())
				.productId(favourite.getProductId())
				.likeDate(favourite.getLikeDate())
				.userDto(
						UserDto.builder()
							.userId(favourite.getUserId())
							.build())
				.productDto(
						ProductDto.builder()
						.productId(favourite.getProductId())
						.build())
				.build();
	}
	
	public static Favourite map(final FavouriteDto favouriteDto) {
		return Favourite.builder()
				.userId(favouriteDto.getUserId())
				.productId(favouriteDto.getProductId())
				.likeDate(favouriteDto.getLikeDate())
				.build();
	}
	
	
	
}










