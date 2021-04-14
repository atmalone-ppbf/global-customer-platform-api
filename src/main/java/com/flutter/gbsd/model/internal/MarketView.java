package com.flutter.gbsd.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MarketView {
    private Long seqNo;

    // event info
    private Long eventId;

    // market info
    private Long marketId;
    private String marketName;
    private Long marketTypeId;

    private MessageType messageType;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class EventEvict {

        protected Long seqNo;

        // event info
        protected Long eventId;
        protected Long eventScheduledStartTime;
        protected Boolean resulted;

        protected Long evictionTimestamp;

    }
}