package com.flutter.gbsi.model.internal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventEvict {

    private Long seqNo;

    // event info
    private Long eventId;
    private Long eventScheduledStartTime;
    private Boolean resulted;

    private Long evictionTimestamp;

}
