package com.flutter.gcpapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class NicknamedAccount {
    String accountId;
    String brand;
}