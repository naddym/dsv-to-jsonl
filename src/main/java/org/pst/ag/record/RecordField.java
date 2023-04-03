package org.pst.ag.record;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordField {
    private String name;
    private String value;
    private boolean isNumericType;
    private boolean isDateType;
}
