package com.asap.server.controller.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMeetingTimeSaveRequestDto implements Serializable {


    @NotNull
    private DateDto date;

    @NotNull
    private List<ScheduleDto> schedule;
}