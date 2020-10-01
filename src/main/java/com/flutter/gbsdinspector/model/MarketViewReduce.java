package com.flutter.gbsdinspector.model;

import org.apache.flink.api.common.functions.ReduceFunction;

import java.util.Optional;

public class MarketViewReduce implements ReduceFunction<MarketView> {

    @Override
    public MarketView reduce(MarketView previous, MarketView current) {
        return MarketView.builder()
                .eventId(Optional.ofNullable(current.getEventId()).orElse(previous.getEventId()))
                .marketId(Optional.ofNullable(current.getMarketId()).orElse(previous.getMarketId()))
                .marketTypeId(Optional.ofNullable(current.getMarketTypeId()).orElse(previous.getMarketTypeId()))
                .build();
    }
}
