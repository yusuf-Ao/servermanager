package io.aycodes.commons.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class WeeklyStat {
    private int serverDownCount;
    private int serverUpCount;
}
