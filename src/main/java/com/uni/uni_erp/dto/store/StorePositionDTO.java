package com.uni.uni_erp.dto.store;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class StorePositionDTO {
    private Integer id; // 포지션 ID
    private String name; // 포지션 이름
    private Integer minRequiredNum; // 최소 요구 인원 수
    private Integer storeId; // 스토어 ID

    public StorePositionDTO(Integer id, String name, Integer minRequiredNum, Integer storeId) {
        this.id = id;
        this.name = name;
        this.minRequiredNum = minRequiredNum;
        this.storeId = storeId;
    }

    public StorePositionDTO(String name, Integer minRequiredNum) {
        this.name = name;
        this.minRequiredNum = minRequiredNum;
    }
}
