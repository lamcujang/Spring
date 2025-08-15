package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.*;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PcErEquestQueryRequest;
import org.common.dbiz.request.productRequest.ProductEquestQueryRequest;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;

import java.util.List;

public interface ProductService extends BaseServiceGeneric<ProductDto,Integer, ProductQueryRequest>{



	GlobalReponse saveAll(SaveAllProductAttr request);

	GlobalReponse saveAllProduct(ListProductDto request);

	GlobalReponsePagination eRequestGetPc(PcErEquestQueryRequest request);

	GlobalReponsePagination eRequestGetProduct(ProductEquestQueryRequest request);

	GlobalReponsePagination KcGetProduct(ProductQueryRequest request);

	GlobalReponse intSave (List<ProductIntDto> request);

	GlobalReponse findByIdOrgAccess(ProductQueryRequest request);

	GlobalReponse findByIdLocator(ProductQueryRequest request);

	GlobalReponsePagination findAllByWarehouse(ProductQueryRequest request);


	GlobalReponsePagination getOnHand(ProductQueryRequest request);

	GlobalReponse savePart(SaveAllProductAttr request);

	GlobalReponsePagination getOrgAssign(ProductQueryRequest request);

	GlobalReponse saveImage(Integer productId, ImageDto imageDto);


	GlobalReponsePagination findAllSimple(ProductQueryRequest request);

	GlobalReponsePagination findAllV2(ProductQueryRequest req);

}
