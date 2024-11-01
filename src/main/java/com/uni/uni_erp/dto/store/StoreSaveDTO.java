package com.uni.uni_erp.dto.store;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StoreSaveDTO {
    private String name;
    private Integer is24Hours;
    private Integer isOpen;
    private String storeAddress;
    private Integer allowMinutes = 1; // 기본값 설정
    private Integer userId; // User ID 추가


    public Store toStore(User user) {
        return Store.builder()
                .name(name)
                .is24Hours(is24Hours)
                .isOpen(isOpen)
                .storeAddress(storeAddress)
                .allowMinutes(allowMinutes)
                .user(user)
                .build();
    }
}
