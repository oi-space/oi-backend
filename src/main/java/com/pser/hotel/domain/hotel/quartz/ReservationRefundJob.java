package com.pser.hotel.domain.hotel.quartz;

import com.pser.hotel.domain.hotel.application.ReservationService;
import com.pser.hotel.global.error.StatusUpdateException;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationRefundJob extends QuartzJobBean {
    private final ReservationService reservationService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        long reservationId = jobDataMap.getLong("reservationId");
        Try.run(() -> reservationService.refund(reservationId))
                .recover(StatusUpdateException.class, (Void) null)
                .get();
    }
}
