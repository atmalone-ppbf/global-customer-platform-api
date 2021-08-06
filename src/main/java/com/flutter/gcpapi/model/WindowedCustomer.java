package com.flutter.gcpapi.model;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WindowedCustomer {
    String accountId;
    String brand;
    String customerNickname;
    int betCount;
    Double totalStaked;
}
