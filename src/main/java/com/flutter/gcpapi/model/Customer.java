package com.flutter.gcpapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {
    String accountId;
    String brand;
    String customerNickname;
    List<Tuple2<Long, Float>> stakeFactors;
    List<String> lastFiveBets;
    long betCount;
    Float currentStakeFactor;
    String liabilityGroup;
    Double totalStaked;
    List<Tuple2<Long, String>> traderComments;

    Map<String, MatchedAccount> matchedAccounts = new HashMap<>();
}
