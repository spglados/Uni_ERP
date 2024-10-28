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

import java.time.LocalDateTime;
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
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.minusHours(18);
        LocalDateTime endOfDay = now.plusHours(18);
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

    public List<AttendanceDTO.ResponseDTO> updateAttendance(Long uniqueEmployeeNumber, Integer storeId, AttendanceDTO.RequestDTO reqDTO) {
        // 1. 해당 사번이 존재 하는지, 식당의 직원인지 확인, 비밀번호 확인
        Employee employeeEntity = employeeRepository.findByUniqueEmployeeNumber(uniqueEmployeeNumber)
                .orElseThrow(() -> new RestException400("해당 직원이 존재하지 않습니다."));
        if (!employeeEntity.getStore().getId().equals(storeId)) {
            throw new RestException400("해당 식당의 직원이 아닙니다.");
        }
        if (!employeeEntity.getPassword().equals(reqDTO.getPassword())) {
            throw new RestException400("비밀번호가 일치하지 않습니다.");
        }
        // id가 있는 경우 업데이트
        if (reqDTO.getId() != null){
            Attendance attendanceEntity = attendanceRepository.findById(reqDTO.getId()).orElseThrow(() -> new RestException400("해당 일정이 존재하지 않습니다."));
            if (reqDTO.getType().equals("attendance")) {
                attendanceEntity.setStatus(Attendance.Status.WORKING);
                LocalDateTime now = LocalDateTime.now();
                attendanceEntity.setStartTime(DateFormatter.toTimestamp(now));
            } else {
                // TODO 퇴근 로직
            }
        // id가 없는 경우 생성
        } else {
            // TODO 고쳐야함
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.minusHours(18);
        LocalDateTime endOfDay = now.plusHours(18);
        List<Attendance> attendanceEntities = attendanceRepository.findByUniqueEmployeeNumberAndTodayExcludingUnplanned(uniqueEmployeeNumber, DateFormatter.toTimestamp(startOfDay), DateFormatter.toTimestamp(endOfDay));
    }
}
