package com.uni.uni_erp.dto.common;

import com.uni.uni_erp.domain.entity.common.Notice;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {

    private Integer id;
    private String category;
    private String title;
    private String content;
    private Timestamp createdAt;
    private int views;

    public String dateFormatter() {
        // Convert Timestamp to LocalDateTime
        LocalDateTime dateTime = createdAt.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public NoticeDTO(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdAt = notice.getCreatedAt();
        this.views = notice.getViews();
    }

}
