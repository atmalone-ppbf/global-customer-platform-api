package com.flutter.gbsdinspector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventView {
    private Long seqNo;

    // event info
    private Long eventId;
    private String eventName;

    private Long eventTypeId;
    private String eventTypeName;

    private Long eventSubclassId;
    private String eventSubclassName;

    private Long eventScheduledStartTime;
}
