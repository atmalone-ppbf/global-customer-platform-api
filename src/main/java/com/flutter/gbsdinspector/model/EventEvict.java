package com.flutter.gbsdinspector.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventEvict {

    protected Long seqNo;

    // event info
    protected Long eventId;
    protected Long eventScheduledStartTime;
    protected Boolean resulted;

    protected Long evictionTimestamp;

}
