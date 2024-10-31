package com.uni.uni_erp.dto;

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

}
