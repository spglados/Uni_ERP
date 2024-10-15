package com.uni.uni_erp.controller.erp;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import com.uni.uni_erp.dto.EmployeeDTO;
import com.uni.uni_erp.service.erp.hr.HrService;
import com.uni.uni_erp.service.erp.hr.ScheduleService;
import com.uni.uni_erp.util.Str.EnumCommonUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/erp/hr")
@RequiredArgsConstructor
public class HrController {

    private final HrService hrService;
    private final ScheduleService scheduleService;
    private final HttpSession session;

    @GetMapping("/employee-register")
    public String employeeRegisterPage(Model model) {
        model.addAttribute("employee", new Employee());
        return "/erp/hr/employeeRegister";
    }

    @PostMapping("/employee-register")
    public String registerEmployee(@RequestBody EmployeeDTO employeeDTO, @RequestParam(name = "storeId") Integer storeId) {
        hrService.registerEmployee(employeeDTO, storeId);
        return "직원이 등록되었습니다.";
    }

    // TODO 스토어로 바꾸기
    @GetMapping("/employee-list")
    public String employeeListPage(HttpSession session, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");
        List<Employee> employees = hrService.getEmployeesByStoreId(storeId);
        model.addAttribute("employees", employees); // 직원 목록을 모델에 추가
        return "/erp/hr/employeeList"; // 직원 목록 페이지 반환
    }

    /**
     *  근무 일정 관리 페이지 호출
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/schedule")
    public String schedulePage(@RequestParam(name = "type", required = false) String type, Model model) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        // 문자열로 받은 type을 enum 타입으로 변환
        Schedule.ScheduleType scheduleType = EnumCommonUtil.getEnumFromString(Schedule.ScheduleType.class, type);
        List<Schedule> schedules = scheduleService.findByStoreIdAndType(storeId, scheduleType);

        model.addAttribute("schedules", schedules);

        return "/erp/hr/schedule";
    }

}
