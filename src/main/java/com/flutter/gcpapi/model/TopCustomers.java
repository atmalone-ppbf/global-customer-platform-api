package com.flutter.gcpapi.model;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TopCustomers {
    public List<WindowedCustomer> TopStakers;
    public List<WindowedCustomer> TopBetters;
}
