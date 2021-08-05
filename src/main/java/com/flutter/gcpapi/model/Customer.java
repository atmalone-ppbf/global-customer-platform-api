package com.flutter.gcpapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

    private String brand;

    // market info
    private Double customerId;

    // selection info
    private String customerNickname;

    private Integer betCount;

    private Object aggregatedMetricsFromBets;

    private String lastFiveBetsDetails;

    private Tuple2<String, String> linkedAccounts;

    private String stakeFactorChangeReason;

    private String liabilityGroup;

    private List<String> StakeFactorChangeHistory;

}
