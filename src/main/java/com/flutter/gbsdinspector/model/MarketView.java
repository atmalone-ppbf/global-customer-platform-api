package com.flutter.gbsdinspector.model;

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
    private String marketTypeName;
}
