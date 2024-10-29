package com.uni.uni_erp.dto.erp.product;

import com.uni.uni_erp.domain.entity.erp.product.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ProductDTO {

    private int id;

    private int userId;

    // UserId + StoreId + pk 형식 유지
    private long productCode;

    private String name;

    private String category;

    private int price;

    private int storeId;

    private MultipartFile image;

    private String description;

    private Integer todaySales;

    private Integer yesterdaySales;

    private Integer monthSales;

    private Integer previousMonthSales;

    private Integer yearSales;

    public String formatToPrice() {
        // DecimalFormat을 사용하여 3자리마다 콤마를 넣고, 앞에 \를 붙임
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(this.price);
    }

    public Product toProduct(Integer userId, Integer storeId) {
        if(userId == null || storeId == null) {
            return null;
        }
        try {
            return Product.builder()
                    .name(name)
                    .category(category)
                    .price(price)
                    // 이미지가 있으면 넣고 없으면 null 처리
                    .image(image != null ? new SerialBlob(this.image.getBytes()) : null)
                    .description(description)
                    .ingredients(new ArrayList<>())
                    .build();
        } catch (SQLException e) {
            log.warn("저장 실패 : " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.warn("연결 실패 : " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSalesDTO {

        private long productCode;
        private int quantity;

    }

}
