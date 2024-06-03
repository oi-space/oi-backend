package com.pser.hotel.domain.hotel.kafka.consumer;

import com.pser.hotel.domain.hotel.dto.ReservationDto;
import com.pser.hotel.domain.hotel.quartz.ReservationClosingJob;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationCreatedConsumer {
    private final Scheduler scheduler;

    @RetryableTopic(kafkaTemplate = "reservationDtoValueKafkaTemplate", attempts = "5")
    @KafkaListener(topics = KafkaTopics.RESERVATION_CREATED, groupId = "${kafka.consumer-group-id}", containerFactory = "reservationDtoValueListenerContainerFactory")
    public void onCreated(ReservationDto reservationDto) throws SchedulerException {
        scheduleClosingJob(reservationDto);
    }

    private void scheduleClosingJob(ReservationDto reservationDto) throws SchedulerException {
        Date closingDate = getClosingDate(reservationDto);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("reservationId", reservationDto.getId());

        JobDetail job = JobBuilder.newJob(ReservationClosingJob.class)
                .withIdentity("reservation.closing.%s".formatted(reservationDto.getId()))
                .setJobData(jobDataMap)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(closingDate)
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    private Date getClosingDate(ReservationDto reservationDto) {
        LocalDate localDate = reservationDto.getEndAt();
        LocalTime localTime = reservationDto.getRoom().getCheckIn();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
