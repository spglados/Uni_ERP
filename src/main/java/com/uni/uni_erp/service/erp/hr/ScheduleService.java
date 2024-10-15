package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.repository.erp.hr.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    /**
     *  상점 id로 일정 조회
     * @param storeId
     * @return
     */
    public List<Schedule> findByStoreIdAndType(Integer storeId, Schedule.ScheduleType type) {
       return scheduleRepository.findByStoreIdAndType(storeId, type);
    }

}
