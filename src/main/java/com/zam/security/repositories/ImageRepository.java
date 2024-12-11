package com.zam.security.repositories;

import com.zam.security.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    public ImageEntity findTop1ByProductProductId(Integer productId);
    @Query(value = """
        SELECT 
            MIN(i.image_id) AS id, 
            i.product_id, 
            i.color_id, 
            i.image_url, 
            i.image_description
        FROM 
            image i
        WHERE 
            i.product_id = :productId
        GROUP BY 
            i.product_id, i.color_id
    """, nativeQuery = true)
    List<Object[]> findImagesGroupedByColor(@Param("productId") Integer productId);

    public List<ImageEntity> findByProductProductIdAndColorColorId(Integer productId, Integer colorId);

    public ImageEntity findTop1ByProductProductIdAndColorColorName(Integer productId, String colorName);

}
