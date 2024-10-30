package com.uni.uni_erp.dto.erp.hr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uni.uni_erp.domain.entity.erp.hr.Holiday;
import com.uni.uni_erp.util.date.DateFormatter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayParsingDTO {

    private BodyWrapper response;

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BodyWrapper {
        private ItemsWrapper body;
    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemsWrapper {
        private ItemWrapper items;
    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemWrapper {
        private List<HolidayDTO> item;
    }

    @Getter
    @Setter
    @ToString
    public static class HolidayDTO {
        private String dateName;
        private String locdate;

        public Holiday toEntity() {
            return Holiday.builder()
                    .type(Holiday.Type.HOLIDAY)
                    .date(LocalDate.parse(locdate, DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .description(dateName)
                    .build();
        }
    }
}