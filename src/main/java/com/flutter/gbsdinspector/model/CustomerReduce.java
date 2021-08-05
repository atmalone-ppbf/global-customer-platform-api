package com.flutter.gbsdinspector.model;

import org.apache.flink.api.common.functions.ReduceFunction;

import java.util.Optional;

public class CustomerReduce implements ReduceFunction<Customer> {

    @Override
    public Customer reduce(Customer previous, Customer current) {
        return Customer.builder()
                .seqNo(current.getSeqNo())
                .selectionId(Optional.ofNullable(current.getSelectionId()).orElse(previous.getSelectionId()))
                .marketId(Optional.ofNullable(current.getMarketId()).orElse(previous.getMarketId()))
                .build();
    }
}
