package ru.edu.dto;

import lombok.Builder;
import lombok.Value;

@Value // геттеры, сеттеры, equals/hashcode, constructor
@Builder
public class PaymentFilter {

  String firstName;
  String lastName;

}
