package com.flutter.gbsi.model;

import com.flutter.gbsd.model.internal.EventView;
import lombok.Builder;
import lombok.Getter;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;

@Builder
@Getter
public class SavepointAnalysis {
    private final Long totalEvents;
    private final Long totalMarkets;
    private final Long totalSelections;

    private final List<EventView> oldestEventViews;
    private final List<EventView> eventsWithoutStartTime;
    private final List<Tuple2<Long, Long>> eventsWithMostMarkets;
    private final List<Tuple2<Long, Long>> marketsWithMostSelections;
}
