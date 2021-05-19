package com.flutter.gbsi.model;

import com.flutter.gbsi.model.internal.SelectionView;
import org.apache.flink.api.common.functions.ReduceFunction;

import java.util.Optional;

public class SelectionViewReduce implements ReduceFunction<SelectionView> {

    @Override
    public SelectionView reduce(SelectionView previous, SelectionView current) {
        return SelectionView.builder()
                .seqNo(current.getSeqNo())
                .selectionId(Optional.ofNullable(current.getSelectionId()).orElse(previous.getSelectionId()))
                .selectionName(Optional.ofNullable(current.getSelectionName()).orElse(previous.getSelectionName()))
                .marketId(Optional.ofNullable(current.getMarketId()).orElse(previous.getMarketId()))
                .build();
    }
}
