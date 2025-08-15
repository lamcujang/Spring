package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Image;
import com.dbiz.app.productservice.domain.Product;
import com.dbiz.app.productservice.domain.ProductCombo;
import com.dbiz.app.productservice.domain.Uom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    Page<Product> findAll(Specification specification, Pageable pageable);

    Page<Product> findAllByTenantId(Pageable pageable, Integer tenantId);

    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findById(Integer productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.id = :productId")
    void deleteById(Integer productId);

    List<Product> findByProductCategoryId(Integer productCategoryId);

    //    @Query(value = "SELECT p.id FROM Product p WHERE p.productCategory.id = :productCategoryId")
    @Query(value = "SELECT p.id FROM Product p WHERE p.productCategoryId = :productCategoryId")
    List<Integer> getIdByProductCategoryId(Integer productCategoryId);

    @Query(value = "SELECT p.id FROM Product p WHERE p.name LIKE %:keyword% or p.code like %:keyword%")
    List<Integer> getIdByKeyWord(String keyword);

    List<Product> findAllByProductParentId(Integer productParentId);

    @Query("select p from Product p where lower(p.name) = lower(:name)")
    Product find(String name);

    @Query("select coalesce( max(p.id),999999) from Product p ")
    Integer getMaxId();

    @Query("SELECT p FROM Product p WHERE p.erpProductId = :erpProductId")
    Optional<Product> findByErpProductId(Integer erpProductId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.name = :name, p.code = :code, p.saleprice = :saleprice, p.description = :description,p.isPurchased=:isPurchased," +
            " p.isSales = :isSales, p.isStocked = :isStocked, p.erpProductId = :erpProductId, p.groupType = :groupType , p.isTopping = :isTopping, p.uom =:uom" +
            " , p.taxId = :taxId ,p.productCategoryId = :productCategoryId, p.isActive =:isActive," +
            "p.image = :image  WHERE p.id = :id")
    int updateProductDetails(@Param("id") Integer id,
                             @Param("name") String name,
                             @Param("code") String code,
                             @Param("saleprice") BigDecimal saleprice,
                             @Param("description") String description,
                             @Param("isPurchased") String isPurchased,
                             @Param("isSales") String isSales,
                             @Param("isStocked") String isStocked,
                             @Param("erpProductId") Integer erpProductId,
                             @Param("groupType") String groupType,
                             @Param("isTopping") String isTopping,
                             @Param("uom") Uom uom,
                             @Param("taxId") Integer taxId,
                             @Param("productCategoryId") Integer productCategoryId,
                             @Param("isActive") String isActive,
                             @Param("image") Image image);

    Boolean existsByTaxId(Integer taxId);

    Boolean existsByCode(String code);

    Product findByCode(String code);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) = LOWER(:name)") // bug?
    List<Product> findByNameIgnoreCase(@Param("name") String name);

    List<Product> findByName(String name);

}
