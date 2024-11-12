package com.uni.uni_erp.service.common;

import com.uni.uni_erp.domain.entity.payment.SubscribeDuration;
import com.uni.uni_erp.repository.common.SubscribeDurationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeDurationService {

    private final SubscribeDurationRepository subscribeDurationRepository;

    @Transactional
    public void updateEndTimeByUserId(Integer userId) {
        LocalDate nextMonthLastDay = LocalDate.now().plusMonths(1).withDayOfMonth(LocalDate.now().plusMonths(1).lengthOfMonth());
        Timestamp nextMonthLastDayTimestamp = Timestamp.valueOf(nextMonthLastDay.atStartOfDay());
        subscribeDurationRepository.updateEndTimeByUserId(userId, nextMonthLastDayTimestamp);
    }

    public Double findAverageSubscribeDuration() {
        List<SubscribeDuration> subscribeDurations = subscribeDurationRepository.findSubscribeDurationsWithEndTime();
        double totalDuration = 0;
        int count = 0;

        for (SubscribeDuration subscribeDuration : subscribeDurations) {
            long duration = subscribeDuration.getEnd().getTime() - subscribeDuration.getStart().getTime();
            totalDuration += duration;
            count++;
        }

        if (count == 0) {
            return (double) count; // or throw an exception, depending on your requirements
        }

        double averageDurationMonth = totalDuration / (1000 * 60 * 60 * 24 / 30.0);
        return Double.valueOf(String.format("%.1f", averageDurationMonth));
    }
}
