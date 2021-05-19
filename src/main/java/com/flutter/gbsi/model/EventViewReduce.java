package com.flutter.gbsi.model;

import com.flutter.gbsi.model.internal.EventView;
import org.apache.flink.api.common.functions.ReduceFunction;

import java.util.Optional;

public class EventViewReduce implements ReduceFunction<EventView> {

    @Override
    public EventView reduce(EventView previous, EventView current) {
        return EventView.builder()
                .seqNo(current.getSeqNo())
                .eventId(Optional.ofNullable(current.getEventId()).orElse(previous.getEventId()))
                .eventName(Optional.ofNullable(current.getEventName()).orElse(previous.getEventName()))
                .eventTypeId(Optional.ofNullable(current.getEventTypeId()).orElse(previous.getEventTypeId()))
                .eventTypeName(Optional.ofNullable(current.getEventTypeName()).orElse(previous.getEventTypeName()))
                .eventSubclassId(Optional.ofNullable(current.getEventSubclassId()).orElse(previous.getEventSubclassId()))
                .eventSubclassName(Optional.ofNullable(current.getEventSubclassName()).orElse(previous.getEventSubclassName()))
                .eventScheduledStartTime(Optional.ofNullable(current.getEventScheduledStartTime()).orElse(previous.getEventScheduledStartTime()))
                .build();
    }
}
