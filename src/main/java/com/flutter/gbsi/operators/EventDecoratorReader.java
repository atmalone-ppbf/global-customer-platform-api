package com.flutter.gbsi.operators;

import com.flutter.gbsi.model.internal.EventView;
import com.flutter.gbsi.model.EventViewReduce;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.state.api.functions.KeyedStateReaderFunction;
import org.apache.flink.util.Collector;

@Slf4j
public class EventDecoratorReader extends KeyedStateReaderFunction<Long, EventView> {

    public static final ReducingStateDescriptor<EventView> EVENT_VIEW_STATE_DESCRIPTOR =
            new ReducingStateDescriptor<>(
                    "eventViewState",
                    new EventViewReduce(),
                    EventView.class
            );

    protected transient ReducingState<EventView> eventViewState;

    @Override
    public void open(Configuration parameters) {
        eventViewState = getRuntimeContext().getReducingState(EVENT_VIEW_STATE_DESCRIPTOR);
    }

    @Override
    public void readKey(Long key, Context context, Collector<EventView> collector) throws Exception {
        collector.collect(eventViewState.get());
    }
}
