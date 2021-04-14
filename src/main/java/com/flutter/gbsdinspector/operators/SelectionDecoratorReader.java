package com.flutter.gbsdinspector.operators;

import com.flutter.gbsd.model.internal.SelectionView;
import com.flutter.gbsdinspector.model.SelectionViewReduce;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.state.api.functions.KeyedStateReaderFunction;
import org.apache.flink.util.Collector;

@Slf4j
public class SelectionDecoratorReader extends KeyedStateReaderFunction<Long, SelectionView> {

    public static final ReducingStateDescriptor<SelectionView> SELECTION_VIEW_STATE_DESCRIPTOR =
            new ReducingStateDescriptor<>(
                    "selectionViewState",
                    new SelectionViewReduce(),
                    SelectionView.class
            );

    protected transient ReducingState<SelectionView> selectionViewState;

    @Override
    public void open(Configuration parameters) {
        selectionViewState = getRuntimeContext().getReducingState(SELECTION_VIEW_STATE_DESCRIPTOR);
    }

    @Override
    public void readKey(Long key, KeyedStateReaderFunction.Context context, Collector<SelectionView> collector) throws Exception {
        collector.collect(selectionViewState.get());
    }
}
