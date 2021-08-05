package com.flutter.gcpapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BetLeg {

    private String betId;
    private Integer totalLegs;

    // event info
    private Long eventId;
    private Long eventTypeId;
    private Long eventSubclassId;
    private Long eventScheduledStartTime;
    private Boolean eventInPlay;

    // market info
    private Long marketId;
    private Long marketTypeId;

    // selection info
    private Long selectionId;
}
