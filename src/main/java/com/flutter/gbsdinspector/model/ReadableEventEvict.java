package com.flutter.gbsdinspector.model;


import com.flutter.gbsd.model.internal.MarketView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadableEventEvict extends MarketView.EventEvict {

    private String readableEvictionTimestamp;
    private String readableEventScheduledStartTime;

    @Builder(builderMethodName = "bbuilder")
    public ReadableEventEvict(Long seqNo, Long eventId, Long eventScheduledStartTime, Boolean resulted, Long evictionTimestamp, String readableEventScheduledStartTime, String readableEvictionTimestamp) {
        super(seqNo, eventId, eventScheduledStartTime, resulted, evictionTimestamp);
        this.readableEventScheduledStartTime = readableEventScheduledStartTime;
        this.readableEvictionTimestamp = readableEvictionTimestamp;
    }

    public static ReadableEventEvict fromEventEvict(MarketView.EventEvict eventEvict) {
        ReadableEventEvict.ReadableEventEvictBuilder readableEventEvictBuilder = ReadableEventEvict.bbuilder()
                .seqNo(eventEvict.getSeqNo())
                .eventId(eventEvict.getEventId())
                .evictionTimestamp(eventEvict.getEvictionTimestamp())
                .eventScheduledStartTime(eventEvict.getEventScheduledStartTime())
                .resulted(eventEvict.getResulted());

        if (eventEvict.getEventScheduledStartTime() != null) {
            readableEventEvictBuilder.readableEventScheduledStartTime(Instant.ofEpochMilli(eventEvict.getEventScheduledStartTime()).toString());
        }

        if (eventEvict.getEvictionTimestamp() != null) {
            readableEventEvictBuilder.readableEvictionTimestamp(Instant.ofEpochMilli(eventEvict.getEvictionTimestamp()).toString());
        }
        return readableEventEvictBuilder.build();
    }
}