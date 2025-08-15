package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.Product;
import com.dbiz.app.productservice.domain.ProductCombo;
import com.dbiz.app.productservice.domain.Uom;
import com.dbiz.app.productservice.helper.Mapper.ProductComboMapper;
import com.dbiz.app.productservice.repository.ProductComboRepository;
import com.dbiz.app.productservice.repository.ProductRepository;
import com.dbiz.app.productservice.repository.UomRepository;
import com.dbiz.app.productservice.service.ProductComboService;
import com.dbiz.app.productservice.specification.ProductComboSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ProductCDto;
import org.common.dbiz.dto.productDto.ProductComboDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductComboQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductComboServiceImpl implements ProductComboService {
    private final ProductComboRepository productComboRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final RequestParamsUtils requestParamsUtils;
    private final ProductRepository productRepository;
    private final ProductComboMapper productComboMapper;
    private final UomRepository uomRepository;

    @Override
    public GlobalReponsePagination findAll(ProductComboQueryRequest query) {

        log.info("*** ProductDto List, service; fetch all products *");

        Pageable pageable = requestParamsUtils.getPageRequest(query);
        Specification<ProductCombo> spec = ProductComboSpecification.getEntitySpecification(query);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<ProductCombo> products = productComboRepository.findAll(spec, pageable);
        List<ProductComboDto> listData = new ArrayList<>();
        for (ProductCombo item : products.getContent()) {
            ProductComboDto productComboDto = modelMapper.map(item, ProductComboDto.class);
            Product product = null;
            if (query.getProductComponentId() != null)
                product = productRepository.findById(item.getProductId()).orElseThrow(() -> new ObjectNotFoundException("Product not found"));
            else
                product = productRepository.findById(item.getProductComponentId()).orElseThrow(() -> new ObjectNotFoundException("Product not found"));
            ProductCDto productDto = modelMapper.map(product, ProductCDto.class);
            productDto.setUomCode(product.getUom().getCode());
            productDto.setUomName(product.getUom().getName());
            productDto.setUomId(product.getUom().getId());
            productComboDto.setComponent(productDto);
            listData.add(productComboDto);
        }
        response.setMessage(messageSource.getMessage("product.combo.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(products.getNumber());
        response.setPageSize(products.getSize());
        response.setTotalPages(products.getTotalPages());
        response.setTotalItems(products.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse save(ProductComboDto productComboDto) {
        log.info("*** Productcombo, service; save product ***");
        GlobalReponse response = new GlobalReponse();
        ProductCombo productSave = productComboMapper.toProductCombo(productComboDto);

        if (productComboDto.getId() != null) // update
        {
            productSave = this.productComboRepository.findById(productComboDto.getId()).orElseThrow(() -> new ObjectNotFoundException("Product not found"));

            modelMapper.map(productComboDto, productSave);
            this.productComboRepository.save(productSave);
            response.setMessage(messageSource.getMessage("product.combo.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
        } else {
            productSave = this.productComboRepository.save(productSave);
            response.setMessage(messageSource.getMessage("product.combo.created", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        }

        response.setData(modelMapper.map(productSave, productComboDto.getClass()));
        log.info("Product saved successfully with ID: {}", productSave.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete productcombo by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<ProductCombo> productDelete = this.productComboRepository.findById(id);
        if (productDelete.isEmpty()) {
            response.setMessage(String.format(messageSource.getMessage("product.combo.not.found", null, LocaleContextHolder.getLocale()), id));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        this.productComboRepository.delete(productDelete.get());
        response.setMessage(messageSource.getMessage("product.combo.deleted", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** ProductDto, service; fetch product by id *");

        GlobalReponse response = new GlobalReponse();
        ProductCombo entity = this.productComboRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("product.combo.not.found", null, LocaleContextHolder.getLocale()), id)));

//       List<ProductComboVLineDto> productComboVLineDtoList =
//        entity.getComponent().stream().map((element) -> modelMapper.map(element, ProductComboVLineDto.class)).collect(Collectors.toList());
//       entity.setComponent(null);
        ProductComboDto dto = modelMapper.map(entity, ProductComboDto.class);
//       dto.setComponent(productComboVLineDtoList);

        response.setData(dto);
        response.setMessage(messageSource.getMessage("product.combo.fetch.success", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findByProductId(Integer id) {
        List<ProductCombo> productCombos = this.productComboRepository.findByProductId(id);
        List<ProductComboDto> reponseData = productCombos.stream()
                .map((element) -> modelMapper.map(element, ProductComboDto.class)).collect(Collectors.toList());
        return GlobalReponse.builder()
                .data(reponseData)
                .message("Product fetched successfully")
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse findByProductIsComponentById(Integer id) {
        List<ProductCombo> productCombos = this.productComboRepository.findByProductIdAndIsItem(id, "Y");
        List<ProductComboDto> reponseData = productCombos.stream()
                .map((element) -> modelMapper.map(element, ProductComboDto.class)).collect(Collectors.toList());
        return GlobalReponse.builder()
                .data(reponseData)
                .message("Product fetched successfully")
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     *
     * @param query
     * @return
     */
    @Override
    public GlobalReponsePagination findAllComponentByProductId(ProductComboQueryRequest query) {
        log.info("*** ProductDto List, service; fetch all products *");
        Pageable pageable = requestParamsUtils.getPageRequest(query);
        Specification<ProductCombo> spec = ProductComboSpecification.getEntitySpecification(query);
        Page<ProductCombo> products = productComboRepository.findAll(spec, pageable);

        GlobalReponsePagination response = new GlobalReponsePagination();
        List<ProductComboDto> listData = new ArrayList<>();
        for (ProductCombo item : products.getContent()) {
            ProductComboDto productComboDto = modelMapper.map(item, ProductComboDto.class);
            Product product = null;
            product = productRepository.findById(item.getProductComponentId()).orElseThrow(() -> new ObjectNotFoundException("Product not found"));
            ProductCDto productDto = modelMapper.map(product, ProductCDto.class);
            productComboDto.setComponent(productDto);
            listData.add(productComboDto);
        }
        response.setMessage(messageSource.getMessage("product.combo.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(products.getNumber());
        response.setPageSize(products.getSize());
        response.setTotalPages(products.getTotalPages());
        response.setTotalItems(products.getTotalElements());
        return response;
    }
}
