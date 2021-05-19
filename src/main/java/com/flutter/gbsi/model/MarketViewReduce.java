package com.flutter.gbsi.model;

import com.flutter.gbsi.model.internal.MarketView;
import org.apache.flink.api.common.functions.ReduceFunction;

import java.util.Optional;

public class MarketViewReduce implements ReduceFunction<MarketView> {

    @Override
    public MarketView reduce(MarketView previous, MarketView current) {
        return MarketView.builder()
                .seqNo(current.getSeqNo())
                .eventId(Optional.ofNullable(current.getEventId()).orElse(previous.getEventId()))
                .marketId(Optional.ofNullable(current.getMarketId()).orElse(previous.getMarketId()))
                .marketName(Optional.ofNullable(current.getMarketName()).orElse(previous.getMarketName()))
                .marketTypeId(Optional.ofNullable(current.getMarketTypeId()).orElse(previous.getMarketTypeId()))
                .build();
    }
}
