package com.asap.server.controller.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TimeSlotInfoDto {
    public List<String> userNames = new ArrayList<>();
    public int weight = 0;
}
