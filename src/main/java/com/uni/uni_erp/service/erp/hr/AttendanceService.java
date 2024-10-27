package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.dto.erp.hr.AttendanceDTO;
import com.uni.uni_erp.exception.errorsRest.RestException400;
import com.uni.uni_erp.repository.erp.hr.AttendanceRepository;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.util.date.DateFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * 사번으로 직원 조회
     *
     * @param uniqueEmployeeNumber 사번
     * @param storeId              해당 식당의 직원인지 확인 위함
     * @return 이름과 사번 반환
     */
    public List<AttendanceDTO.ResponseDTO> findEmployeeByUniqueEmployeeNumber(Long uniqueEmployeeNumber, Integer storeId) {
        // 1. 해당 사번이 존재 하는지, 식당의 직원인지 확인
        Employee employeeEntity = employeeRepository.findByUniqueEmployeeNumber(uniqueEmployeeNumber)
                .orElseThrow(() -> new RestException400("해당 직원이 존재하지 않습니다."));
        if (!employeeEntity.getStore().getId().equals(storeId)) {
            throw new RestException400("해당 식당의 직원이 아닙니다.");
        }
        // 2. 해당 사번으로 오늘 근무 조회
        // TODO 만약 store에 시간 개념이 생길 경우 변경해야함
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime startOfDay;
        LocalDateTime endOfDay;
        // 현재 시간이 6시 이전이면 전날 6시 ~ 오늘 6시 범위를 조회
        if (nowTime.isBefore(LocalTime.of(6, 0))) {
            LocalDate yesterday = today.minusDays(1);
            startOfDay = yesterday.atTime(6, 0);
            endOfDay = today.atTime(6, 0);
        } else {
            // 현재 시간이 6시 이후면 오늘 6시 ~ 내일 6시 범위를 조회
            startOfDay = today.atTime(6, 0);
            endOfDay = today.plusDays(1).atTime(6, 0);
        }
        List<Attendance> attendanceEntities = attendanceRepository.findByUniqueEmployeeNumberAndTodayExcludingUnplanned(uniqueEmployeeNumber, DateFormatter.toTimestamp(startOfDay), DateFormatter.toTimestamp(endOfDay));
        if (attendanceEntities.isEmpty()) {
            // 조회된 근무가 없음
            List<AttendanceDTO.ResponseDTO> attendanceDTOList = new ArrayList<>();
            AttendanceDTO.ResponseDTO attendanceDTO = new AttendanceDTO.ResponseDTO();
            attendanceDTO.setName(employeeEntity.getName());
            attendanceDTO.setStatus("UNPLANNED_WORK");
            attendanceDTOList.add(attendanceDTO);
            return attendanceDTOList;
        }
        return attendanceEntities.stream().map(AttendanceDTO.ResponseDTO::new).toList();
    }

}
