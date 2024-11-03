package com.uni.uni_erp.domain.entity.erp.hr;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "holiday_tb")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        SUNDAY("일요일"),
        HOLIDAY("공휴일");

        private final String description;
    }
}
