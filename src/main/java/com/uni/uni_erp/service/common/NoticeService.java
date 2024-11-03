package com.uni.uni_erp.service.common;

import com.uni.uni_erp.domain.entity.Notice;
import com.uni.uni_erp.dto.NoticeDTO;
import com.uni.uni_erp.repository.common.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.jasper.tagplugins.jstl.core.If;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<NoticeDTO> getNotices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notice> notices = noticeRepository.findAll(pageable);
        return notices.map(this::convertToDTO);
    }

    private NoticeDTO convertToDTO(Notice notice) {
        NoticeDTO dto = new NoticeDTO();
        dto.setId(notice.getId());
        dto.setCategory(String.valueOf(notice.getCategory()));
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setCreatedAt(notice.getCreatedAt());
        dto.setViews(notice.getViews());
        return dto;
    }

    @Transactional
    public void save(NoticeDTO noticeDTO) {
        Notice notice = Notice.builder()
                .category(noticeDTO.getCategory().equals("업데이트") ? Notice.Categorys.업데이트 : Notice.Categorys.안내)
                .title(noticeDTO.getTitle())
                .content(noticeDTO.getContent())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .views(0)
                .build();
        noticeRepository.save(notice);
    }

    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    @Transactional
    public void delete(Integer noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    public NoticeDTO findOne(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);
        if(notice == null) {
            return null;
        }
        return convertToDTO(notice);
    }
}
