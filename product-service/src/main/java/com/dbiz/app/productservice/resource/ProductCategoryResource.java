package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.ProductCatIntDto;
import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.common.dbiz.dto.productDto.request.ProductCategoryReq;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductCategoryQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categoryProducts")
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryResource {
    private final ProductCategoryService categoryService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute ProductCategoryQueryRequest  request)
    {
        log.info("*** category List, resource; fetch all category *");
        return ResponseEntity.ok(this.categoryService.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > saveCategory(@RequestBody ProductCategoryDto  saveCategoryDto)
    {
        log.info("*** category List, resource; save category *");
        return ResponseEntity.ok(this.categoryService.save(saveCategoryDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable Integer id)
    {
        log.info("*** category List, controller; delete category by id *");
        return ResponseEntity.ok(this.categoryService.deleteById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody ProductCategoryDto saveCategoryDto)
    {
        log.info("*** category List, controller; update category *");
        return ResponseEntity.ok(this.categoryService.save(saveCategoryDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable Integer id)
    {
        log.info("*** category List, controller; fetch category by id *");
        return ResponseEntity.ok(this.categoryService.findById(id));
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intProductCategory(@RequestBody ProductCatIntDto dtoList)
    {
        log.info("*** category List, resource; save category *");
        return ResponseEntity.ok(this.categoryService.intSave(dtoList));
    }

    @GetMapping("/getOrgAccess")
    public ResponseEntity<GlobalReponse> getOrgAccess(@ModelAttribute ProductCategoryQueryRequest request)
    {
        log.info("*** category List, resource; fetch all category *");
        return ResponseEntity.ok(this.categoryService.getOrgAccess(request));
    }

    @PostMapping("/org/access")
    public ResponseEntity<GlobalReponsePagination> getPcTerminalAccess(@RequestBody ProductCategoryReq req)
    {
        log.info("*** category List, resource; fetch all category *");
        return ResponseEntity.ok(this.categoryService.getPcTerminalAccess(req));
    }

    @GetMapping("/findAllSimple")
    public ResponseEntity<GlobalReponsePagination> getAllSimple(@ModelAttribute ProductCategoryQueryRequest request)
    {
        log.info("*** category List, resource; fetch all category *");
        return ResponseEntity.ok(this.categoryService.getAllSimple(request));
    }
}
