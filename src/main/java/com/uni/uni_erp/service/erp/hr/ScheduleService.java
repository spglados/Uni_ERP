package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.domain.entity.erp.product.Store;
import com.uni.uni_erp.dto.erp.hr.ScheduleDTO;
import com.uni.uni_erp.exception.errors.Exception400;
import com.uni.uni_erp.exception.errors.Exception500;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.repository.erp.hr.ScheduleRepository;
import com.uni.uni_erp.repository.store.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StoreRepository storeRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * 일정 조회
     * @param storeId 세션에 담긴 상점 id
     * @param type 계획, 완료, null (전부 조회)
     *             전부 조회시 시간 비교 로직추가
     * @return DTO 형태로 List 반환
     */
    public List<ScheduleDTO.ResponseDTO> findByStoreIdAndType(Integer storeId, Schedule.Status type) {
        // TODO TYPE에 따라 로직 변경 필요
        List<Schedule> scheduleEntities = scheduleRepository.findByStoreIdAndType(storeId, type);
        if (scheduleEntities == null || scheduleEntities.isEmpty()) {
            return new ArrayList<>();
        }
        return scheduleEntities.stream()
                .map(Schedule::toResponseDTO)
                .toList();
    }

    /**
     * 일정 등록
     * @param reqDTO 받아온 일정 데이터
     * @param storeId 세션에 담긴 상점 id
     * @return 생성한 일정을 DTO 형태로 반환
     */
    @Transactional
    public ScheduleDTO.ResponseDTO create(ScheduleDTO.CreateDTO reqDTO, Integer storeId) {
        Schedule scheduleEntity = null;
        try {
            Store storeEntity = storeRepository.findById(storeId).orElseThrow(() -> new Exception400("식당 정보가 없습니다."));
            Employee employeeEntity = employeeRepository.findById(reqDTO.getEmpId()).orElseThrow(() -> new Exception400("해당 직원이 없습니다."));
            scheduleEntity = scheduleRepository.save(reqDTO.toEntity(storeEntity, employeeEntity));
        } catch (DataIntegrityViolationException e) {
            throw new Exception400("데이터 무결성 위반으로 스케줄 생성에 실패했습니다.");
        } catch (JpaSystemException e) {
            throw new Exception500("데이터베이스 시스템 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new Exception500("스케줄 생성 중 알 수 없는 오류가 발생했습니다.");
        }
        return scheduleEntity.toResponseDTO();
    }

}
