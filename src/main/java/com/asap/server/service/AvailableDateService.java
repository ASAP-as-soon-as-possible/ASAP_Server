package com.asap.server.service;

import com.asap.server.common.utils.DateUtil;
import com.asap.server.controller.dto.response.AvailableDateResponseDto;
import com.asap.server.controller.dto.response.AvailableDatesDto;
import com.asap.server.controller.dto.response.TimeSlotDto;
import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.MeetingV2;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.AvailableDateRepository;
import com.asap.server.service.vo.TimeBlockVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailableDateService {
    private final AvailableDateRepository availableDateRepository;
    private final TimeBlockService timeBlockService;
    private final TimeBlockUserService timeBlockUserService;

    private LocalDate dateFormatter(final String stringOfDate) {
        return LocalDate.parse(stringOfDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public List<AvailableDateResponseDto> getAvailableDates(final MeetingV2 meetingV2) {

        return findAvailableDateByMeeting(meetingV2)
                .stream()
        List<AvailableDate> availableDates = findAvailableDates(meetingV2);

        return availableDates.stream()
                .map(availableDate ->
                        AvailableDateResponseDto.builder()
                                .month(DateUtil.getMonth(availableDate.getDate()))
                                .day(DateUtil.getDay(availableDate.getDate()))
                                .dayOfWeek(DateUtil.getDayOfWeek(availableDate.getDate()))
                                .build())
                .collect(Collectors.toList());
    }

    public List<AvailableDate> findAvailableDateByMeeting(final MeetingV2 meetingV2) {
        List<AvailableDate> availableDates = availableDateRepository.findByMeeting(meetingV2);

        if (availableDates.isEmpty()) throw new NotFoundException(Error.AVAILABLE_DATE_NOT_FOUND_EXCEPTION);

        return availableDates;
    }

    public AvailableDatesDto getAvailableDatesDto(final AvailableDate availableDate, final int memberCount) {
        List<TimeSlotDto> timeSlotDtos = timeBlockService.findByAvailableDate(availableDate).stream().map(
                timeBlock -> timeBlockUserService.getTimeSlotDto(timeBlock, memberCount)
        ).collect(Collectors.toList());

        return AvailableDatesDto.builder()
                .timeSlots(timeSlotDtos)
                .month(DateUtil.getMonth(availableDate.getDate()))
                .day(DateUtil.getDay(availableDate.getDate()))
                .dayOfWeek(DateUtil.getDayOfWeek(availableDate.getDate()))
                .build();
    }

    ;

    public AvailableDate findByMeetingAndDate(final MeetingV2 meetingV2,
                                              final String month,
                                              final String day) {
        return availableDateRepository.findByMeetingAndDate(meetingV2,
                        DateUtil.transformLocalDate(month, day))
                .orElseThrow(() -> new NotFoundException(Error.AVAILABLE_DATE_NOT_FOUND_EXCEPTION));
    }

    public List<TimeBlocksByDateVo> getAvailableDateVos(final MeetingV2 meetingV2) {
        List<AvailableDate> availableDates = findAvailableDates(meetingV2);

        return availableDates.stream()
                .map(availableDate -> {
                    List<TimeBlockVo> timeBlocks = timeBlockService
                            .getTimeBlocksByAvailableDate(availableDate)
                            .stream()
                            .map(TimeBlockVo::of)
                            .collect(Collectors.toList());

                    return TimeBlocksByDateVo.of(availableDate, timeBlocks);
                }).collect(Collectors.toList());
    }

    private List<AvailableDate> findAvailableDates(final MeetingV2 meeting) {
        List<AvailableDate> availableDates = availableDateRepository.findByMeeting(meeting);

        if (availableDates.isEmpty()) throw new NotFoundException(Error.AVAILABLE_DATE_NOT_FOUND_EXCEPTION);
        return availableDates;
    }

    public void create(final MeetingV2 meeting, final List<String> availableDates) {
        availableDates
                .stream()
                .map(s -> availableDateRepository.save(
                        AvailableDate.builder()
                                .meeting(meeting)
                                .date(dateFormatter(s.substring(0, 10)))
                                .build()
                )).collect(Collectors.toList());
    }
}
