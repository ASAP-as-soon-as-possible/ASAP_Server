package com.asap.server.service;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.BadRequestException;
import com.asap.server.common.exception.model.NotFoundException;
import com.asap.server.common.utils.DateUtil;
import com.asap.server.persistence.domain.AvailableDate;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.repository.AvailableDateRepository;
import com.asap.server.presentation.controller.dto.response.AvailableDateResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailableDateService {
    private static final String DATE_SEPARATOR = "/";
    private static final int MONTH_ELEMENT_INDEX = 1;
    private static final int DAY_ELEMENT_INDEX = 2;

    private final AvailableDateRepository availableDateRepository;


    public List<AvailableDateResponseDto> getAvailableDates(final Meeting meeting) {
        List<AvailableDate> availableDates = findAvailableDates(meeting);

        return availableDates.stream()
                .map(availableDate ->
                        AvailableDateResponseDto.builder()
                                .month(DateUtil.getMonth(availableDate.getDate()))
                                .day(DateUtil.getDay(availableDate.getDate()))
                                .dayOfWeek(DateUtil.getDayOfWeek(availableDate.getDate()))
                                .build())
                .collect(Collectors.toList());
    }

    public List<AvailableDate> findAvailableDateByMeeting(final Meeting meeting) {
        List<AvailableDate> availableDates = availableDateRepository.findByMeeting(meeting);

        if (availableDates.isEmpty()) throw new NotFoundException(Error.AVAILABLE_DATE_NOT_FOUND_EXCEPTION);

        return availableDates;
    }

    private List<AvailableDate> findAvailableDates(final Meeting meeting) {
        List<AvailableDate> availableDates = availableDateRepository.findByMeeting(meeting);

        if (availableDates.isEmpty()) throw new NotFoundException(Error.AVAILABLE_DATE_NOT_FOUND_EXCEPTION);
        return availableDates;
    }

    public void create(final Meeting meeting, final List<String> availableDates) {
        if (isDuplicatedDate(availableDates)) throw new BadRequestException(Error.DUPLICATED_DATE_EXCEPTION);

        availableDates.stream()
                .sorted()
                .forEach(dateFormat -> createAvailableDate(dateFormat, meeting));
    }

    private void createAvailableDate(final String dateFormat, final Meeting meeting) {
        String[] dateElements = dateFormat.split(DATE_SEPARATOR);
        String month = dateElements[MONTH_ELEMENT_INDEX];
        String day = dateElements[DAY_ELEMENT_INDEX];

        availableDateRepository.save(
                AvailableDate.builder()
                        .meeting(meeting)
                        .date(DateUtil.transformLocalDate(month, day))
                        .build()
        );
    }

    private boolean isDuplicatedDate(final List<String> availableDates) {
        return availableDates.size() != availableDates.stream().distinct().count();
    }
}
