package com.uni.uni_erp.controller.common;

import com.uni.uni_erp.domain.entity.Notice;
import com.uni.uni_erp.service.common.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    public String noticePage(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Page<Notice> notices = noticeService.getNotices(page, size);

        model.addAttribute("notices", notices);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", notices.getTotalPages());
        model.addAttribute("pageSize", size);

        return "/common/notice";
    }
}
