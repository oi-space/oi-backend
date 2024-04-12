package com.psear.hotel.global.config.aop;

import com.psear.hotel.global.config.aop.log.LogTrace;
import com.psear.hotel.global.config.aop.log.LogTraceAspect;
import com.psear.hotel.global.config.aop.log.ThreadLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {
    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLogTrace();
    }
}
