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
    private Long eventTypeId;
    private Long eventSubclassId;
    private Long eventScheduledStartTime;
}
