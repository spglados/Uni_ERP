package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.dto.erp.hr.AttendanceDTO;
import com.uni.uni_erp.exception.errorsRest.RestException400;
import com.uni.uni_erp.exception.errorsRest.RestException401;
import com.uni.uni_erp.repository.erp.hr.AttendanceRepository;
import com.uni.uni_erp.repository.erp.hr.EmployeeRepository;
import com.uni.uni_erp.util.date.DateFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * 사번으로 근무 조회
     *
     * @param uniqueEmployeeNumber 사번
     * @param storeId              해당 식당의 직원인지 확인 위함
     * @return 조회된 근무 일정 반환
     */
    @Transactional
    public List<AttendanceDTO.ResponseDTO> findSchedulesByUniqueEmployeeNumber(Long uniqueEmployeeNumber, Integer storeId) {
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
            attendanceDTO.setStatus("UNPLANNED");
            attendanceDTOList.add(attendanceDTO);
            return attendanceDTOList;
        }
        return attendanceEntities.stream().map(AttendanceDTO.ResponseDTO::new).toList();
    }

    /**
     * 출퇴근 등록
     *
     * @param uniqueEmployeeNumber 사번
     * @param storeId              식당
     * @param reqDTO               출퇴근 여부, 패스워드
     * @return 업데이트된 출퇴근 정보 반환
     */
    @Transactional
    public List<AttendanceDTO.ResponseDTO> attendanceProc(Long uniqueEmployeeNumber, Integer storeId, AttendanceDTO.RequestDTO reqDTO) {
        // 1. 해당 사번이 존재 하는지, 식당의 직원인지 확인, 비밀번호 확인
        Employee employeeEntity = employeeRepository.findByUniqueEmployeeNumber(uniqueEmployeeNumber)
                .orElseThrow(() -> new RestException400("해당 직원이 존재하지 않습니다."));
        if (!employeeEntity.getStore().getId().equals(storeId)) {
            throw new RestException400("해당 식당의 직원이 아닙니다.");
        }
        if (!employeeEntity.getPassword().equals(reqDTO.getPassword())) {
            throw new RestException401("비밀번호가 일치하지 않습니다.");
        }
        // id가 있는 경우 업데이트
        if (reqDTO.getId() != null) {
            Attendance attendanceEntity = attendanceRepository.findById(reqDTO.getId()).orElseThrow(() -> new RestException400("해당 일정이 존재하지 않습니다."));
            if (reqDTO.getType().equals("attendance")) {
                attendanceEntity.setStartTime(DateFormatter.toTimestamp(LocalDateTime.now()));
                attendanceEntity.setStatus(Attendance.Status.WORKING);
            } else {
                // 퇴근 로직
                attendanceEntity.setEndTime(DateFormatter.toTimestamp(LocalDateTime.now()));
                setValuesAtLeave(attendanceEntity);
            }
        } else {
            // id가 없는 경우 (연동 스케줄이 없는 출근) 생성
            attendanceRepository.save(Attendance.builder()
                    .store(employeeEntity.getStore())
                    .employee(employeeEntity)
                    .startTime(DateFormatter.toTimestamp(LocalDateTime.now()))
                    .status(Attendance.Status.WORKING)
                    .build());
        }
        LocalDateTime startOfDay = LocalDateTime.now().minusHours(18);
        LocalDateTime endOfDay = LocalDateTime.now().plusHours(18);
        List<Attendance> attendanceEntities = attendanceRepository.findByUniqueEmployeeNumberAndTodayExcludingUnplanned(uniqueEmployeeNumber, DateFormatter.toTimestamp(startOfDay), DateFormatter.toTimestamp(endOfDay));
        return attendanceEntities.stream().map(AttendanceDTO.ResponseDTO::new).toList();
    }

    /**
     * 퇴근 이후의 처리
     * 1. 정산용 근무 시간 계산
     * 2. 연결된 일정 상태 변경
     * 3. 근무 상태 변경
     * 4. 지각 조퇴시 해당 시간 저장
     * 5. 초과 시간 저장
     * 6. 시급 세팅
     *
     * @param attendance 출석 엔티티
     */
    @Transactional
    public void setValuesAtLeave(Attendance attendance) {
        Schedule schedule = attendance.getSchedule();

        // 휴식 시간 및 실 근무 시간 세팅 - 공통
        LocalDateTime attendanceTime = DateFormatter.toLocalDateTime(attendance.getStartTime());
        LocalDateTime leaveTime = DateFormatter.toLocalDateTime(attendance.getEndTime());
        int minutesDifference = (int) Duration.between(attendanceTime, leaveTime).toMinutes();
        attendance.setBreakTime((minutesDifference / 240) * 30);
        attendance.setWorkTime(minutesDifference - attendance.getBreakTime());

        // 계획된 근무 일 경우 처리
        if (schedule != null) {
            LocalDateTime plannedStartTime = DateFormatter.toLocalDateTime(schedule.getStartTime());
            LocalDateTime plannedEndTime = DateFormatter.toLocalDateTime(schedule.getEndTime());
            schedule.setStatus(Schedule.Status.COMPLETED);
            // 지각 시간 (분 단위 계산)
            int lateMinutes = (int) Duration.between(plannedStartTime, attendanceTime).toMinutes();

            // 조퇴 시간 (분 단위 계산)
            int earlyLeaveMinutes = (int) Duration.between(leaveTime, plannedEndTime).toMinutes();
            // 허용 범위
            int allowMinutes = attendance.getStore().getAllowMinutes();
            if (lateMinutes > allowMinutes && earlyLeaveMinutes > allowMinutes) {
                attendance.setStatus(Attendance.Status.LATE_AND_LEFT_EARLY);
                attendance.setMissedTime(earlyLeaveMinutes + lateMinutes);
            } else if (lateMinutes > allowMinutes) {
                attendance.setStatus(Attendance.Status.LATE);
                attendance.setMissedTime(lateMinutes);
            } else if (earlyLeaveMinutes > allowMinutes) {
                attendance.setStatus(Attendance.Status.LEFT_EARLY);
                attendance.setMissedTime(earlyLeaveMinutes);
            } else {
                attendance.setStatus(Attendance.Status.ATTENDED);
            }
            // 일찍 온 시간
            int earlyAttendanceMinutes = (int) Duration.between(attendanceTime, plannedStartTime).toMinutes();
            // 늦게 간 시간
            int lateLeaveMinutes = (int) Duration.between(plannedEndTime, leaveTime).toMinutes();
            // 초과 시간 계산
            attendance.setOveredTime(earlyAttendanceMinutes + lateLeaveMinutes);
        } else {
            attendance.setStatus(Attendance.Status.UNPLANNED_WORK);
        }
        attendance.setWage(attendance.getEmployee().getWage());
    }

    /**
     * 근태 관리 페이지용 조회
     * @param storeId 상점 필터
     * @param startDate 시작 날짜 (디폴트 : 3개월 전)
     * @param endDate 종료 날짜 (디폴트 : 오늘)
     * @return 그리드용 DTO 반환
     */
    public List<AttendanceDTO.GridDTO> findAttendanceList(Integer storeId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        List<Attendance> attendanceList =  attendanceRepository.findByStoreIdAndDateRange(storeId, startDate, endDate);
        if (attendanceList.isEmpty()) {
            return null;
        }
        return attendanceList.stream().map(AttendanceDTO.GridDTO::new).toList();
    }

    public AttendanceDTO.ErpMainDTO getAttendanceForMain(Integer storeId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.withHour(6).withMinute(0);
        LocalDateTime tomorrow = now.plusDays(1).withHour(6).withMinute(0);
        if (now.getHour() < 6) {
            today = today.minusDays(1);
            tomorrow = tomorrow.plusDays(1);
        }
        List<Attendance> attendancesToday = attendanceRepository.findByStoreIdAndDateTimeRange(storeId, today, tomorrow);
        LocalDate nowDate = LocalDate.now();
        // 이번 달 1일
        LocalDate firstDayOfMonth = nowDate.withDayOfMonth(1);

        // 이번 달 마지막 날
        int lastDay = YearMonth.from(nowDate).lengthOfMonth();  // 이번 달의 마지막 날짜 (예: 28, 30, 31)
        LocalDate lastDayOfMonth = nowDate.withDayOfMonth(lastDay);
        List<Attendance> attendancesMonth = attendanceRepository.findByStoreIdAndDateRange(storeId, firstDayOfMonth, lastDayOfMonth);
        Map<Long, List<Attendance>> groupedByEmployee = attendancesMonth.stream()
                .collect(Collectors.groupingBy(attendance -> attendance.getEmployee().getUniqueEmployeeNumber()));
        List<AttendanceDTO.MonthlyAttendanceDTO> monthlyAttendanceDTOList = new ArrayList<>();
        for (Map.Entry<Long, List<Attendance>> entry : groupedByEmployee.entrySet()) {
            Long empId = entry.getKey();
            List<Attendance> empAttendances = entry.getValue();

            String empName = empAttendances.get(0).getEmployee().getName(); // 이름은 첫 번째 Attendance에서 가져옴
            int lateCount = (int) empAttendances.stream()
                    .filter(att -> att.getStatus() == Attendance.Status.LATE
                            || att.getStatus() == Attendance.Status.LATE_AND_LEFT_EARLY)
                    .count();
            int earlyLeaveCount = (int) empAttendances.stream()
                    .filter(att -> att.getStatus() == Attendance.Status.LEFT_EARLY
                            || att.getStatus() == Attendance.Status.LATE_AND_LEFT_EARLY)
                    .count();
            int unauthorizedAbsentCount = (int) empAttendances.stream()
                    .filter(att -> att.getStatus() == Attendance.Status.UNAUTHORIZED_ABSENT)
                    .count();

            // DTO 객체 생성 후 리스트에 추가
            monthlyAttendanceDTOList.add(new AttendanceDTO.MonthlyAttendanceDTO(empId, empName, lateCount, earlyLeaveCount, unauthorizedAbsentCount));
        }
        return new AttendanceDTO.ErpMainDTO(attendancesToday.stream().map(AttendanceDTO.TodayAttendanceDTO::new).toList(), monthlyAttendanceDTOList);
    }
}
