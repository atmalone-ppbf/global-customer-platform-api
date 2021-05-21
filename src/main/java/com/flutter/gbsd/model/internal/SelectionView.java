package com.flutter.gbsd.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SelectionView {

    private Long seqNo;

    // market info
    private Long marketId;

    // selection info
    private Long selectionId;
    private String selectionName;

    private MessageType messageType;
}
