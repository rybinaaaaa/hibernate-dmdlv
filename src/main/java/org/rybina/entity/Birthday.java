package org.rybina.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record Birthday(LocalDate birthday) {

    public Long getAge() {
        return ChronoUnit.YEARS.between(birthday, LocalDate.now());
    }
}
