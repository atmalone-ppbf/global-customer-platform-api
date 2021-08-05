package com.flutter.gcpapi.model;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class MatchedAccount {
    String accountId;
    String brand;
}