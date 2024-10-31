package com.uni.uni_erp.controller.user;

import com.uni.uni_erp.dto.store.StorePositionDTO;
import com.uni.uni_erp.dto.store.StoreUpdateDTO;
import com.uni.uni_erp.service.user.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/erp/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final HttpSession session;

    @GetMapping("/correction")
    public String storeUpdate(Model model, HttpSession session) {
        Integer storeId = (Integer) session.getAttribute("storeId");

        if (storeId != null) {
            StoreUpdateDTO store = storeService.getStoreById(storeId); // 가게 정보 조회
            List<StorePositionDTO> positions = storeService.getPositionsByStoreId(storeId); // 가게에 대한 포지션 조회
            model.addAttribute("store", store); // 모델에 가게 정보 추가
            model.addAttribute("storePositionDTO", new StorePositionDTO()); // 새로운 포지션 DTO 추가
            model.addAttribute("positions", positions); // 포지션 목록 추가
        } else {
            return "redirect:/error"; // storeId가 없을 경우 에러 페이지로 리다이렉트
        }

        return "/erp/store/storeCorrection"; // 수정 페이지로 이동
    }


    @PostMapping("/update")
    public String updateStore(@ModelAttribute StoreUpdateDTO storeUpdateDTO, RedirectAttributes redirectAttributes) {
        System.out.println("Received Store DTO: " + storeUpdateDTO);

        try {
            // 가게 수정 서비스 호출
            storeService.updateStore(storeUpdateDTO.getId(), storeUpdateDTO);

            // 성공 메시지 추가
            redirectAttributes.addFlashAttribute("message", "가게 정보 수정완료");

            // 수정 페이지로 리다이렉트
            return "redirect:/erp/store/correction"; // 수정 페이지로 리다이렉트
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "수정 중 오류 발생: " + e.getMessage());
            return "redirect:/erp/store/correction"; // 수정 페이지로 리다이렉트
        }
    }

    // 포지션 등록
    @PostMapping("/position/create")
    public String createPosition(@ModelAttribute StorePositionDTO storePositionDTO, RedirectAttributes redirectAttributes) {
        try {
            storeService.createPosition(storePositionDTO); // 포지션 생성 서비스 호출
            redirectAttributes.addFlashAttribute("message", "포지션 등록 완료");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "등록 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/erp/store/correction"; // 수정 페이지로 리다이렉트
    }

    // 포지션 수정
    @PostMapping("/position/update")
    public String updatePosition(@ModelAttribute StorePositionDTO storePositionDTO, RedirectAttributes redirectAttributes) {
        try {
            storeService.updatePosition(storePositionDTO.getId(), storePositionDTO);
            redirectAttributes.addFlashAttribute("message", "포지션 수정 완료");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "포지션 수정 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/erp/store/correction"; // 수정 페이지로 리다이렉트
    }

    @PostMapping("/position/delete/{positionId}")
    public String deletePosition(@PathVariable("positionId") Integer positionId, RedirectAttributes redirectAttributes) {
        try {
            storeService.deletePosition(positionId); // 포지션 삭제 서비스 호출
            redirectAttributes.addFlashAttribute("message", "포지션 삭제 완료");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "삭제 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/erp/store/correction"; // 수정 페이지로 리다이렉트
    }
}




