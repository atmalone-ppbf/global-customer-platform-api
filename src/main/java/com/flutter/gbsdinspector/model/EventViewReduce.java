package com.flutter.gbsdinspector.model;

import org.apache.flink.api.common.functions.ReduceFunction;

import java.util.Optional;

public class EventViewReduce implements ReduceFunction<EventView> {

    @Override
    public EventView reduce(EventView previous, EventView current) {
        return EventView.builder()
                .seqNo(current.getSeqNo())
                .eventId(Optional.ofNullable(current.getEventId()).orElse(previous.getEventId()))
                .eventTypeId(Optional.ofNullable(current.getEventTypeId()).orElse(previous.getEventTypeId()))
                .eventSubclassId(Optional.ofNullable(current.getEventSubclassId()).orElse(previous.getEventSubclassId()))
                .eventScheduledStartTime(Optional.ofNullable(current.getEventScheduledStartTime()).orElse(previous.getEventScheduledStartTime()))
                .eventInPlay(Optional.ofNullable(current.getEventInPlay()).orElse(previous.getEventInPlay()))
                .build();
    }
}
