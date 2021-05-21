package com.flutter.gbsi.operators;

import com.flutter.gbsd.model.internal.MarketView;
import com.flutter.gbsi.model.MarketViewReduce;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.state.api.functions.KeyedStateReaderFunction;
import org.apache.flink.util.Collector;

@Slf4j
public class MarketDecoratorReader extends KeyedStateReaderFunction<Long, MarketView> {

    public static final ReducingStateDescriptor<MarketView> MARKET_VIEW_STATE_DESCRIPTOR =
            new ReducingStateDescriptor<>(
                    "marketViewState",
                    new MarketViewReduce(),
                    MarketView.class
            );

    protected transient ReducingState<MarketView> marketViewState;

    @Override
    public void open(Configuration parameters) {
        marketViewState = getRuntimeContext().getReducingState(MARKET_VIEW_STATE_DESCRIPTOR);
    }

    @Override
    public void readKey(Long key, KeyedStateReaderFunction.Context context, Collector<MarketView> collector) throws Exception {
        collector.collect(marketViewState.get());
    }
}
