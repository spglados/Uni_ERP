package com.uni.uni_erp.service.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Holiday;
import com.uni.uni_erp.dto.erp.hr.HolidayParsingDTO;
import com.uni.uni_erp.exception.errorsRest.RestException500;
import com.uni.uni_erp.repository.erp.hr.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    // TODO @Value("${api.key.data_kkh}")
    private String serviceKey;

    private final HolidayRepository holidayRepository;

    /**
     * 1. 공공 데이터 포탈 공휴일 정보 api 파싱
     *
     * @param localDate 해당 달의 1일 정보를 담고 있음
     */
    @Transactional
    public Integer setHolidayByParsing(LocalDate localDate) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = UriComponentsBuilder.fromHttpUrl("https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("solYear", localDate.getYear())
                    .queryParam("solMonth", localDate.format(DateTimeFormatter.ofPattern("MM")))
                    .queryParam("_type", "json")
                    .build(false)
                    .toString();
            ResponseEntity<HolidayParsingDTO> response = restTemplate.getForEntity(url, HolidayParsingDTO.class);
            List<HolidayParsingDTO.HolidayDTO> holidayList = response.getBody().getResponse().getBody().getItems().getItem();
            List<Holiday> holidays = holidayRepository.saveAll(holidayList.stream().map(HolidayParsingDTO.HolidayDTO::toEntity).toList());
            return holidays.size();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException500("공휴일 정보를 받아오는 중 오류 발생");
        }
    }

    @Transactional
    public Integer setSunday(LocalDate localDate) {
        List<LocalDate> sundays = new ArrayList<>();
        // 해당 월의 첫 번째 일요일 찾기
        LocalDate sunday = localDate.with(DayOfWeek.SUNDAY);
        if (sunday.isBefore(localDate)) {
            sunday = sunday.plusWeeks(1);  // 첫 번째 일요일이 전달에 속하는 경우 다음 일요일로 이동
        }
        // 해당 월 내 모든 일요일을 리스트에 추가
        while (sunday.getMonth() == localDate.getMonth()) {
            sundays.add(sunday);
            sunday = sunday.plusWeeks(1);  // 1주일씩 증가
        }
        List<Holiday> holidays = holidayRepository.saveAll(
                sundays.stream()
                        .map(date -> Holiday.builder()
                                .date(date)
                                .description("일요일")
                                .type(Holiday.Type.SUNDAY)
                                .build()
                        )
                        .toList());
        return holidays.size();
    }
}
