package com.flutter.gbsdinspector.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadableEventEvict extends EventEvict {

    private String readableEvictionTimestamp;
    private String readableEventScheduledStartTime;

    @Builder (builderMethodName = "bbuilder")
    public ReadableEventEvict(Long seqNo, Long eventId, Long eventScheduledStartTime, Boolean resulted, Long evictionTimestamp, String readableEventScheduledStartTime, String readableEvictionTimestamp) {
        super(seqNo, eventId, eventScheduledStartTime, resulted, evictionTimestamp);
        this.readableEventScheduledStartTime = readableEventScheduledStartTime;
        this.readableEvictionTimestamp = readableEvictionTimestamp;
    }

    public static ReadableEventEvict fromEventEvict(EventEvict eventEvict) {
        return ReadableEventEvict.bbuilder()
                .seqNo(eventEvict.getSeqNo())
                .eventId(eventEvict.getEventId())
                .evictionTimestamp(eventEvict.getEvictionTimestamp())
                .eventScheduledStartTime(eventEvict.getEventScheduledStartTime())
                .resulted(eventEvict.getResulted())
                .readableEventScheduledStartTime(Instant.ofEpochMilli(eventEvict.getEventScheduledStartTime()).toString())
                .readableEvictionTimestamp(Instant.ofEpochMilli(eventEvict.getEvictionTimestamp()).toString())
                .build();
    }
}
