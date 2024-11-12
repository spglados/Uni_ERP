package com.uni.uni_erp.controller.common;

import com.uni.uni_erp.dto.common.NoticeDTO;
import com.uni.uni_erp.service.common.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("")
    public String noticePage(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Page<NoticeDTO> notices = noticeService.getNotices(page, size);

        model.addAttribute("notices", notices);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", notices.getTotalPages());
        model.addAttribute("pageSize", size);

        return "/common/notice";
    }

    @GetMapping("/detail")
    public String noticeDetailPage(@RequestParam("id") Integer id, Model model) {
        NoticeDTO dto = noticeService.findOne(id);
        if(dto == null) {
            return "/common/notice";
        } else {
            model.addAttribute("notice", dto);
        }
        return "/common/noticeDetail";
    }

}