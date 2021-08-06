package com.flutter.gcpapi.model;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NicknamedAccount {
    String accountId;
    String brand;
}