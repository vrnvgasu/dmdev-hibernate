package ru.edu.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// record с Java 14
// https://javarush.com/groups/posts/3827-kofe-breyk-128-rukovodstvo-po-ispoljhzovaniju-java-records
public record Birthday(LocalDate birthday) {

  public long getAge() {
    return ChronoUnit.YEARS.between(birthday, LocalDate.now());
  }

}
